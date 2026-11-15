import React, {useEffect, useState} from 'react';
import {useParams, useNavigate, Link as RouterLink} from 'react-router-dom';
import {
  Box,
  Button,
  Container,
  FormControl,
  FormHelperText,
  InputLabel,
  MenuItem,
  Paper,
  Select,
  SelectChangeEvent,
  TextField,
  Typography
} from '@mui/material';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import SaveIcon from '@mui/icons-material/Save';
import scrumTeamService from '../../services/scrumTeamService';
import teamService, {Member} from '../../services/teamService';

const ScrumTeamCreate: React.FC = () => {
  const {productId} = useParams<{productId: string}>();
  const navigate = useNavigate();
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);
  const [members, setMembers] = useState<Member[]>([]);

  // Form state
  const [name, setName] = useState<string>('');
  const [businessDomain, setBusinessDomain] = useState<string>('');
  const [poEmployeeId, setPoEmployeeId] = useState<string>('');
  const [poName, setPoName] = useState<string>('');

  // Form validation
  const [nameError, setNameError] = useState<string>('');
  const [businessDomainError, setBusinessDomainError] = useState<string>('');

  useEffect(() => {
    const fetchData = async () => {
      try {
        const membersData = await teamService.getAllMembers();
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

    if (!name.trim()) {
      setNameError('Team name is required');
      isValid = false;
    } else {
      setNameError('');
    }

    if (!businessDomain.trim()) {
      setBusinessDomainError('Business domain is required');
      isValid = false;
    } else {
      setBusinessDomainError('');
    }

    return isValid;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!validateForm()) {
      return;
    }

    if (!productId) return;
    setLoading(true);

    try {
      const selectedMember = poEmployeeId ? members.find(m => m.memberId === poEmployeeId) : undefined;

      const newTeam = {
        productId,
        name,
        businessDomain,
        poEmployeeId: selectedMember?.memberId,
        poName: selectedMember?.name
      };

      await scrumTeamService.createScrumTeam(newTeam);
      navigate(`/products/${productId}/scrum-teams`);
    } catch (err) {
      setError('Failed to create scrum team. Please try again later.');
      console.error('Error creating scrum team:', err);
    } finally {
      setLoading(false);
    }
  };

  const handlePoChange = (e: SelectChangeEvent<string>) => {
    const selectedId = e.target.value;
    setPoEmployeeId(selectedId);
    const selectedMember = members.find(m => m.memberId === selectedId);
    setPoName(selectedMember?.name || '');
  };

  return (
      <Container maxWidth="md">
        <Box sx={{my: 4}}>
          <Box display="flex" alignItems="center" mb={4}>
            <Button
                component={RouterLink}
                to={`/products/${productId}/scrum-teams`}
                startIcon={<ArrowBackIcon/>}
                sx={{mr: 2}}
            >
              Back to List
            </Button>
            <Typography variant="h4" component="h1" gutterBottom>
              Create Scrum Team
            </Typography>
          </Box>

          {error && (
              <Paper sx={{p: 2, mb: 2, bgcolor: 'error.light', color: 'error.contrastText'}}>
                <Typography>{error}</Typography>
              </Paper>
          )}

          <Paper sx={{p: 4}}>
            <form onSubmit={handleSubmit}>
              <TextField
                  fullWidth
                  label="Team Name"
                  value={name}
                  onChange={(e) => setName(e.target.value)}
                  error={!!nameError}
                  helperText={nameError}
                  sx={{mb: 3}}
                  required
              />

              <TextField
                  fullWidth
                  label="Business Domain"
                  value={businessDomain}
                  onChange={(e) => setBusinessDomain(e.target.value)}
                  error={!!businessDomainError}
                  helperText={businessDomainError}
                  sx={{mb: 3}}
                  required
              />

              <FormControl fullWidth sx={{mb: 3}} error={!!poEmployeeId}>
                <InputLabel>Product Owner (PO)</InputLabel>
                <Select
                    value={poEmployeeId}
                    onChange={handlePoChange}
                    label="Product Owner (PO)"
                >
                  <MenuItem value="">
                    <em>Select an employee</em>
                  </MenuItem>
                  {members.map((member) => (
                      <MenuItem key={member.memberId} value={member.memberId}>
                        {member.name} ({member.email})
                      </MenuItem>
                  ))}
                </Select>
                <FormHelperText>Optional: Select a Product Owner from employees</FormHelperText>
              </FormControl>

              <Box display="flex" justifyContent="flex-end" gap={2}>
                <Button
                    component={RouterLink}
                    to={`/products/${productId}/scrum-teams`}
                    variant="outlined"
                >
                  Cancel
                </Button>
                <Button
                    type="submit"
                    variant="contained"
                    color="primary"
                    startIcon={<SaveIcon/>}
                    disabled={loading}
                >
                  {loading ? 'Creating...' : 'Create Team'}
                </Button>
              </Box>
            </form>
          </Paper>
        </Box>
      </Container>
  );
};

export default ScrumTeamCreate;
