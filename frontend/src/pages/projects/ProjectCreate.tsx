import React, {useEffect, useState} from 'react';
import {Link as RouterLink, useNavigate} from 'react-router-dom';
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

const ProjectCreate: React.FC = () => {
  const navigate = useNavigate();
  const [loading, setLoading] = useState<boolean>(false);
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
      try {
        const [productsData, membersData] = await Promise.all([
          productService.getAllProducts(),
          teamService.getAllMembers()
        ]);
        setProducts(productsData);
        setMembers(membersData);
      } catch (err) {
        setError('Failed to load form data. Please try again later.');
        console.error('Error loading form data:', err);
      }
    };

    fetchData();
  }, []);

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

    if (!validateForm()) {
      return;
    }

    setLoading(true);

    try {
      const selectedManager = members.find(m => m.memberId === managerId);
      const selectedProduct = productId ? products.find(p => p.productId === productId) : undefined;

      if (!selectedManager) {
        throw new Error('Selected manager not found');
      }

      const newProject = {
        name,
        description,
        status: 'ACTIVE' as const,
        projectManager: {
          id: selectedManager.memberId,
          name: selectedManager.name
        },
        product: selectedProduct ? {
          productId: selectedProduct.productId,
          name: selectedProduct.name
        } : undefined
      };

      await projectService.createProject(newProject);
      navigate('/projects');
    } catch (err) {
      setError('Failed to create project. Please try again later.');
      console.error('Error creating project:', err);
    } finally {
      setLoading(false);
    }
  };

  return (
      <Container maxWidth="md">
        <Box sx={{my: 4}}>
          <Button
              component={RouterLink}
              to="/projects"
              startIcon={<ArrowBackIcon/>}
              sx={{mb: 2}}
          >
            Back to Projects
          </Button>

          <Typography variant="h4" component="h1" gutterBottom>
            Create New Project
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
                        disabled={loading}
                    >
                      {loading ? 'Creating...' : 'Create Project'}
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

export default ProjectCreate;
