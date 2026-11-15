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
import scrumTeamService, {ScrumTeam} from '../../services/scrumTeamService';
import teamService, {Member} from '../../services/teamService';

const ScrumTeamEdit: React.FC = () => {
  const {productId, scrumTeamId} = useParams<{productId: string; scrumTeamId: string}>();
  const navigate = useNavigate();
  const [loading, setLoading] = useState<boolean>(false);
  const [fetching, setFetching] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [members, setMembers] = useState<Member[]>([]);
  const [team, setTeam] = useState<ScrumTeam | null>(null);

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
      if (!productId || !scrumTeamId) return;
      try {
        setFetching(true);
        const [teamData, membersData] = await Promise.all([
          scrumTeamService.getScrumTeamById(productId, scrumTeamId),
          teamService.getAllMembers()
        ]);
        setTeam(teamData);
        setMembers(membersData);
        setName(teamData.name);
        setBusinessDomain(teamData.businessDomain);
        setPoEmployeeId(teamData.poEmployeeId || '');
        setPoName(teamData.poName || '');
      } catch (err) {
        setError('Failed to load form data. Please try again later.');
        console.error('Error loading form data:', err);
      } finally {
        setFetching(false);
      }
    };

    fetchData();
  }, [productId, scrumTeamId]);

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

    if (!productId || !scrumTeamId) return;
    setLoading(true);

    try {
      const selectedMember = poEmployeeId ? members.find(m => m.memberId === poEmployeeId) : undefined;

      const updateDto = {
        name,
        businessDomain,
        poEmployeeId: selectedMember?.memberId,
        poName: selectedMember?.name
      };

      await scrumTeamService.updateScrumTeam(productId, scrumTeamId, updateDto);
      navigate(`/products/${productId}/scrum-teams`);
    } catch (err) {
      setError('Failed to update scrum team. Please try again later.');
      console.error('Error updating scrum team:', err);
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

  const handleUnassignPo = async () => {
    if (!productId || !scrumTeamId) return;
    if (!window.confirm('Are you sure you want to unassign the Product Owner?')) return;
    try {
      await scrumTeamService.unassignPo(productId, scrumTeamId);
      setPoEmployeeId('');
      setPoName('');
    } catch (err) {
      setError('Failed to unassign PO. Please try again later.');
      console.error('Error unassigning PO:', err);
    }
  };

  if (fetching) {
    return (
        <Container maxWidth="md">
          <Box sx={{my: 4}}>
            <Typography>Loading...</Typography>
          </Box>
        </Container>
    );
  }

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
              Edit Scrum Team
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

              <FormControl fullWidth sx={{mb: 3}}>
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

              {poEmployeeId && (
                  <Box sx={{mb: 3}}>
                    <Button
                        onClick={handleUnassignPo}
                        color="warning"
                        variant="outlined"
                        size="small"
                    >
                      Unassign Product Owner
                    </Button>
                  </Box>
              )}

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
                  {loading ? 'Saving...' : 'Save Changes'}
                </Button>
              </Box>
            </form>
          </Paper>
        </Box>
      </Container>
  );
};

export default ScrumTeamEdit;
