import React, {useEffect, useState} from 'react';
import {Link as RouterLink, useNavigate, useParams} from 'react-router-dom';
import {
  Box,
  Button,
  Container,
  FormControl,
  FormHelperText,
  Grid,
  InputLabel,
  MenuItem,
  Paper,
  Select,
  TextField,
  Typography
} from '@mui/material';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import SaveIcon from '@mui/icons-material/Save';
import projectService from '../../services/projectService';
import productService, {Product} from '../../services/productService';
import teamService, {Member} from '../../services/teamService';

const ProjectEdit: React.FC = () => {
  const {id} = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [loading, setLoading] = useState<boolean>(true);
  const [saving, setSaving] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);
  const [products, setProducts] = useState<Product[]>([]);
  const [members, setMembers] = useState<Member[]>([]);

  // Form state
  const [name, setName] = useState<string>('');
  const [description, setDescription] = useState<string>('');
  const [productId, setProductId] = useState<string>('');
  const [managerId, setManagerId] = useState<string>('');

  // Form validation
  const [nameError, setNameError] = useState<string>('');
  const [managerError, setManagerError] = useState<string>('');

  useEffect(() => {
    const fetchData = async () => {
      if (!id) return;

      try {
        setLoading(true);
        const [projectData, productsData, membersData] = await Promise.all([
          projectService.getProjectById(id),
          productService.getAllProducts(),
          teamService.getAllMembers()
        ]);

        // Set form values from project data
        setName(projectData.name);
        setDescription(projectData.description || '');
        setManagerId(projectData.projectManager.id);
        setProductId(projectData.product?.productId || '');

        // Set dropdown options
        setProducts(productsData);
        setMembers(membersData);

        setError(null);
      } catch (err) {
        setError('Failed to load project data. Please try again later.');
        console.error('Error loading project data:', err);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [id]);

  const validateForm = (): boolean => {
    let isValid = true;

    // Validate name
    if (!name.trim()) {
      setNameError('Project name is required');
      isValid = false;
    } else {
      setNameError('');
    }

    // Validate project manager
    if (!managerId) {
      setManagerError('Project manager is required');
      isValid = false;
    } else {
      setManagerError('');
    }

    return isValid;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!id || !validateForm()) {
      return;
    }

    setSaving(true);

    try {
      const selectedManager = members.find(m => m.memberId === managerId);
      const selectedProduct = productId ? products.find(p => p.productId === productId) : undefined;

      if (!selectedManager) {
        throw new Error('Selected manager not found');
      }

      const updatedProject = {
        name,
        description,
        projectManager: {
          id: selectedManager.memberId,
          name: selectedManager.name
        },
        product: selectedProduct ? {
          productId: selectedProduct.productId,
          name: selectedProduct.name
        } : undefined
      };

      await projectService.updateProject(id, updatedProject);
      navigate(`/projects/${id}`);
    } catch (err) {
      setError('Failed to update project. Please try again later.');
      console.error('Error updating project:', err);
    } finally {
      setSaving(false);
    }
  };

  if (loading) {
    return (
        <Container maxWidth="md">
          <Box sx={{my: 4}}>
            <Typography>Loading project data...</Typography>
          </Box>
        </Container>
    );
  }

  return (
      <Container maxWidth="md">
        <Box sx={{my: 4}}>
          <Button
              component={RouterLink}
              to={`/projects/${id}`}
              startIcon={<ArrowBackIcon/>}
              sx={{mb: 2}}
          >
            Back to Project
          </Button>

          <Typography variant="h4" component="h1" gutterBottom>
            Edit Project
          </Typography>

          {error && (
              <Paper sx={{p: 2, mb: 2, bgcolor: 'error.light', color: 'error.contrastText'}}>
                <Typography>{error}</Typography>
              </Paper>
          )}

          <Paper sx={{p: 3}}>
            <form onSubmit={handleSubmit}>
              <Grid container spacing={3}>
                <Grid item xs={12}>
                  <TextField
                      fullWidth
                      label="Project Name"
                      value={name}
                      onChange={(e) => setName(e.target.value)}
                      error={!!nameError}
                      helperText={nameError}
                      required
                  />
                </Grid>

                <Grid item xs={12}>
                  <TextField
                      fullWidth
                      label="Description"
                      value={description}
                      onChange={(e) => setDescription(e.target.value)}
                      multiline
                      rows={4}
                  />
                </Grid>

                <Grid item xs={12} md={6}>
                  <FormControl fullWidth error={!!managerError}>
                    <InputLabel id="project-manager-label">Project Manager</InputLabel>
                    <Select
                        labelId="project-manager-label"
                        value={managerId}
                        onChange={(e) => setManagerId(e.target.value as string)}
                        label="Project Manager"
                        required
                    >
                      {members.map((member) => (
                          <MenuItem key={member.memberId} value={member.memberId}>
                            {member.name}
                          </MenuItem>
                      ))}
                    </Select>
                    {managerError && <FormHelperText>{managerError}</FormHelperText>}
                  </FormControl>
                </Grid>

                <Grid item xs={12} md={6}>
                  <FormControl fullWidth>
                    <InputLabel id="product-label">Product (Optional)</InputLabel>
                    <Select
                        labelId="product-label"
                        value={productId}
                        onChange={(e) => setProductId(e.target.value as string)}
                        label="Product (Optional)"
                    >
                      <MenuItem value="">
                        <em>None</em>
                      </MenuItem>
                      {products.map((product) => (
                          <MenuItem key={product.productId} value={product.productId}>
                            {product.name}
                          </MenuItem>
                      ))}
                    </Select>
                  </FormControl>
                </Grid>

                <Grid item xs={12}>
                  <Box sx={{display: 'flex', justifyContent: 'flex-end'}}>
                    <Button
                        type="submit"
                        variant="contained"
                        color="primary"
                        startIcon={<SaveIcon/>}
                        disabled={saving}
                    >
                      {saving ? 'Saving...' : 'Save Changes'}
                    </Button>
                  </Box>
                </Grid>
              </Grid>
            </form>
          </Paper>
        </Box>
      </Container>
  );
};

export default ProjectEdit;
