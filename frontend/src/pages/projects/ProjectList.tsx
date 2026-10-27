import React, {useEffect, useState} from 'react';
import {Link as RouterLink} from 'react-router-dom';
import {
  Box,
  Button,
  Chip,
  Container,
  Link,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Typography
} from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import ArchiveIcon from '@mui/icons-material/Archive';
import UnarchiveIcon from '@mui/icons-material/Unarchive';
import EditIcon from '@mui/icons-material/Edit';
import projectService, {Project} from '../../services/projectService';

const ProjectList: React.FC = () => {
  const [projects, setProjects] = useState<Project[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  const fetchProjects = async () => {
    try {
      setLoading(true);
      const data = await projectService.getAllProjects();
      setProjects(data);
      setError(null);
    } catch (err) {
      setError('Failed to fetch projects. Please try again later.');
      console.error('Error fetching projects:', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchProjects();
  }, []);

  const handleArchiveProject = async (projectId: string) => {
    try {
      await projectService.archiveProject(projectId);
      fetchProjects(); // Refresh the list
    } catch (err) {
      setError('Failed to archive project. Please try again later.');
      console.error('Error archiving project:', err);
    }
  };

  const handleActivateProject = async (projectId: string) => {
    try {
      await projectService.activateProject(projectId);
      fetchProjects(); // Refresh the list
    } catch (err) {
      setError('Failed to activate project. Please try again later.');
      console.error('Error activating project:', err);
    }
  };

  return (
      <Container maxWidth="lg">
        <Box sx={{my: 4}}>
          <Box display="flex" justifyContent="space-between" alignItems="center" mb={4}>
            <Typography variant="h4" component="h1" gutterBottom>
              Projects
            </Typography>
            <Button
                component={RouterLink}
                to="/projects/create"
                variant="contained"
                color="primary"
                startIcon={<AddIcon/>}
            >
              Create Project
            </Button>
          </Box>

          {error && (
              <Paper sx={{p: 2, mb: 2, bgcolor: 'error.light', color: 'error.contrastText'}}>
                <Typography>{error}</Typography>
              </Paper>
          )}

          {loading ? (
              <Typography>Loading projects...</Typography>
          ) : projects.length === 0 ? (
              <Paper sx={{p: 4, textAlign: 'center'}}>
                <Typography variant="h6">No projects found</Typography>
                <Typography variant="body1" sx={{mt: 2}}>
                  Get started by creating your first project.
                </Typography>
              </Paper>
          ) : (
              <TableContainer component={Paper}>
                <Table>
                  <TableHead>
                    <TableRow>
                      <TableCell>Name</TableCell>
                      <TableCell>Description</TableCell>
                      <TableCell>Status</TableCell>
                      <TableCell>Project Manager</TableCell>
                      <TableCell>Product</TableCell>
                      <TableCell align="right">Actions</TableCell>
                    </TableRow>
                  </TableHead>
                  <TableBody>
                    {projects.map((project) => (
                        <TableRow key={project.projectId}>
                          <TableCell>
                            <Link component={RouterLink} to={`/projects/${project.projectId}`}>
                              {project.name}
                            </Link>
                          </TableCell>
                          <TableCell>{project.description || 'No description'}</TableCell>
                          <TableCell>
                            <Chip
                                label={project.status}
                                color={project.status === 'ACTIVE' ? 'success' : 'default'}
                            />
                          </TableCell>
                          <TableCell>{project.projectManager.name}</TableCell>
                          <TableCell>
                            {project.product ? (
                                <Link component={RouterLink}
                                      to={`/products/${project.product.productId}`}>
                                  {project.product.name}
                                </Link>
                            ) : (
                                'No product'
                            )}
                          </TableCell>
                          <TableCell align="right">
                            <Box sx={{display: 'flex', justifyContent: 'flex-end'}}>
                              <Button
                                  component={RouterLink}
                                  to={`/projects/edit/${project.projectId}`}
                                  startIcon={<EditIcon/>}
                                  sx={{mr: 1}}
                              >
                                Edit
                              </Button>
                              {project.status === 'ACTIVE' ? (
                                  <Button
                                      startIcon={<ArchiveIcon/>}
                                      color="warning"
                                      onClick={() => handleArchiveProject(project.projectId)}
                                  >
                                    Archive
                                  </Button>
                              ) : (
                                  <Button
                                      startIcon={<UnarchiveIcon/>}
                                      color="success"
                                      onClick={() => handleActivateProject(project.projectId)}
                                  >
                                    Activate
                                  </Button>
                              )}
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

export default ProjectList;
