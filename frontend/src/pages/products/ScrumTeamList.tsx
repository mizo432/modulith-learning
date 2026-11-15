import React, {useEffect, useState} from 'react';
import {Link as RouterLink, useParams, useNavigate} from 'react-router-dom';
import {
  Box,
  Button,
  Container,
  Link,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Typography,
  IconButton,
  Tooltip
} from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import scrumTeamService, {ScrumTeam} from '../../services/scrumTeamService';

const ScrumTeamList: React.FC = () => {
  const {productId} = useParams<{productId: string}>();
  const navigate = useNavigate();
  const [scrumTeams, setScrumTeams] = useState<ScrumTeam[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  const fetchScrumTeams = async () => {
    if (!productId) return;
    try {
      setLoading(true);
      const data = await scrumTeamService.getScrumTeamsByProductId(productId);
      setScrumTeams(data);
      setError(null);
    } catch (err) {
      setError('Failed to fetch scrum teams. Please try again later.');
      console.error('Error fetching scrum teams:', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchScrumTeams();
  }, [productId]);

  const handleDeleteScrumTeam = async (scrumTeamId: string) => {
    if (!productId) return;
    if (!window.confirm('Are you sure you want to delete this scrum team?')) return;
    try {
      await scrumTeamService.deleteScrumTeam(productId, scrumTeamId);
      fetchScrumTeams();
    } catch (err) {
      setError('Failed to delete scrum team. Please try again later.');
      console.error('Error deleting scrum team:', err);
    }
  };

  return (
      <Container maxWidth="lg">
        <Box sx={{my: 4}}>
          <Box display="flex" justifyContent="space-between" alignItems="center" mb={4}>
            <Box>
              <Typography variant="h6" color="text.secondary" gutterBottom>
                <Link component={RouterLink} to="/products" sx={{textDecoration: 'none'}}>
                  Products
                </Link>
                {' / '}Scrum Teams
              </Typography>
              <Typography variant="h4" component="h1" gutterBottom>
                Scrum Teams
              </Typography>
            </Box>
            <Button
                component={RouterLink}
                to={`/products/${productId}/scrum-teams/create`}
                variant="contained"
                color="primary"
                startIcon={<AddIcon/>}
            >
              Create Scrum Team
            </Button>
          </Box>

          {error && (
              <Paper sx={{p: 2, mb: 2, bgcolor: 'error.light', color: 'error.contrastText'}}>
                <Typography>{error}</Typography>
              </Paper>
          )}

          {loading ? (
              <Typography>Loading scrum teams...</Typography>
          ) : scrumTeams.length === 0 ? (
              <Paper sx={{p: 4, textAlign: 'center'}}>
                <Typography variant="h6">No scrum teams found</Typography>
                <Typography variant="body1" sx={{mt: 2}}>
                  Get started by creating your first scrum team.
                </Typography>
              </Paper>
          ) : (
              <TableContainer component={Paper}>
                <Table>
                  <TableHead>
                    <TableRow>
                      <TableCell>Team Name</TableCell>
                      <TableCell>Business Domain</TableCell>
                      <TableCell>Product Owner (PO)</TableCell>
                      <TableCell>Created At</TableCell>
                      <TableCell align="right">Actions</TableCell>
                    </TableRow>
                  </TableHead>
                  <TableBody>
                    {scrumTeams.map((team) => (
                        <TableRow key={team.id}>
                          <TableCell>{team.name}</TableCell>
                          <TableCell>{team.businessDomain}</TableCell>
                          <TableCell>{team.poName || 'Not assigned'}</TableCell>
                          <TableCell>{new Date(team.createdAt).toLocaleDateString()}</TableCell>
                          <TableCell align="right">
                            <Box sx={{display: 'flex', justifyContent: 'flex-end'}}>
                              <Tooltip title="Edit">
                                <IconButton
                                    component={RouterLink}
                                    to={`/products/${productId}/scrum-teams/edit/${team.id}`}
                                    size="small"
                                    sx={{mr: 1}}
                                >
                                  <EditIcon/>
                                </IconButton>
                              </Tooltip>
                              <Tooltip title="Delete">
                                <IconButton
                                    onClick={() => handleDeleteScrumTeam(team.id)}
                                    size="small"
                                    color="error"
                                >
                                  <DeleteIcon/>
                                </IconButton>
                              </Tooltip>
                            </Box>
                          </TableCell>
                        </TableRow>
                    ))}
                  </TableBody>
                </Table>
              </TableContainer>
          )}
        </Box>
      </Container>
  );
};

export default ScrumTeamList;
