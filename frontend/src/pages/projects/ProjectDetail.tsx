import React, {useEffect, useState} from 'react';
import {Link as RouterLink, useParams} from 'react-router-dom';
import {
  Box,
  Button,
  Card,
  CardContent,
  Chip,
  Container,
  Grid,
  Link,
  Paper,
  Tab,
  Tabs,
  Typography
} from '@mui/material';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import EditIcon from '@mui/icons-material/Edit';
import ArchiveIcon from '@mui/icons-material/Archive';
import UnarchiveIcon from '@mui/icons-material/Unarchive';
import projectService, {Project} from '../../services/projectService';

interface TabPanelProps {
  children?: React.ReactNode;
  index: number;
  value: number;
}

function TabPanel(props: TabPanelProps) {
  const {children, value, index, ...other} = props;

  return (
      <div
          role="tabpanel"
          hidden={value !== index}
          id={`project-tabpanel-${index}`}
          aria-labelledby={`project-tab-${index}`}
          {...other}
      >
        {value === index && <Box sx={{p: 3}}>{children}</Box>}
      </div>
  );
}

const ProjectDetail: React.FC = () => {
  const {id} = useParams<{ id: string }>();
  const [project, setProject] = useState<Project | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [tabValue, setTabValue] = useState(0);

  const fetchProject = async () => {
    if (!id) return;

    try {
      setLoading(true);
      const data = await projectService.getProjectById(id);
      setProject(data);
      setError(null);
    } catch (err) {
      setError('Failed to fetch project details. Please try again later.');
      console.error('Error fetching project:', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchProject();
  }, [id]);

  const handleTabChange = (event: React.SyntheticEvent, newValue: number) => {
    setTabValue(newValue);
  };

  const handleArchiveProject = async () => {
    if (!project) return;

    try {
      await projectService.archiveProject(project.projectId);
      fetchProject(); // Refresh the data
    } catch (err) {
      setError('Failed to archive project. Please try again later.');
      console.error('Error archiving project:', err);
    }
  };

  const handleActivateProject = async () => {
    if (!project) return;

    try {
      await projectService.activateProject(project.projectId);
      fetchProject(); // Refresh the data
    } catch (err) {
      setError('Failed to activate project. Please try again later.');
      console.error('Error activating project:', err);
    }
  };

  if (loading) {
    return (
        <Container maxWidth="lg">
          <Box sx={{my: 4}}>
            <Typography>Loading project details...</Typography>
          </Box>
        </Container>
    );
  }

  if (error || !project) {
    return (
        <Container maxWidth="lg">
          <Box sx={{my: 4}}>
            <Button
                component={RouterLink}
                to="/projects"
                startIcon={<ArrowBackIcon/>}
                sx={{mb: 2}}
            >
              Back to Projects
            </Button>
            <Paper sx={{p: 2, bgcolor: 'error.light', color: 'error.contrastText'}}>
              <Typography>{error || 'Project not found'}</Typography>
            </Paper>
          </Box>
        </Container>
    );
  }

  return (
      <Container maxWidth="lg">
        <Box sx={{my: 4}}>
          <Button
              component={RouterLink}
              to="/projects"
              startIcon={<ArrowBackIcon/>}
              sx={{mb: 2}}
          >
            Back to Projects
          </Button>

          <Box display="flex" justifyContent="space-between" alignItems="center" mb={4}>
            <Typography variant="h4" component="h1" gutterBottom>
              {project.name}
            </Typography>
            <Box>
              <Button
                  component={RouterLink}
                  to={`/projects/edit/${project.projectId}`}
                  startIcon={<EditIcon/>}
                  variant="outlined"
                  sx={{mr: 1}}
              >
                Edit
              </Button>
              {project.status === 'ACTIVE' ? (
                  <Button
                      startIcon={<ArchiveIcon/>}
                      variant="contained"
                      color="warning"
                      onClick={handleArchiveProject}
                  >
                    Archive
                  </Button>
              ) : (
                  <Button
                      startIcon={<UnarchiveIcon/>}
                      variant="contained"
                      color="success"
                      onClick={handleActivateProject}
                  >
                    Activate
                  </Button>
              )}
            </Box>
          </Box>

          <Card sx={{mb: 4}}>
            <CardContent>
              <Grid container spacing={2}>
                <Grid item xs={12} md={6}>
                  <Typography variant="subtitle1" color="text.secondary">
                    Status
                  </Typography>
                  <Chip
                      label={project.status}
                      color={project.status === 'ACTIVE' ? 'success' : 'default'}
                      sx={{mt: 1}}
                  />
                </Grid>
                <Grid item xs={12} md={6}>
                  <Typography variant="subtitle1" color="text.secondary">
                    Project Manager
                  </Typography>
                  <Typography variant="body1" sx={{mt: 1}}>
                    {project.projectManager.name}
                  </Typography>
                </Grid>
                <Grid item xs={12} md={6}>
                  <Typography variant="subtitle1" color="text.secondary">
                    Product
                  </Typography>
                  <Typography variant="body1" sx={{mt: 1}}>
                    {project.product ? (
                        <Link component={RouterLink} to={`/products/${project.product.productId}`}>
                          {project.product.name}
                        </Link>
                    ) : (
                        'No product associated'
                    )}
                  </Typography>
                </Grid>
                <Grid item xs={12}>
                  <Typography variant="subtitle1" color="text.secondary">
                    Description
                  </Typography>
                  <Typography variant="body1" sx={{mt: 1}}>
                    {project.description || 'No description provided'}
                  </Typography>
                </Grid>
              </Grid>
            </CardContent>
          </Card>

          <Box sx={{borderBottom: 1, borderColor: 'divider'}}>
            <Tabs value={tabValue} onChange={handleTabChange} aria-label="project tabs">
              <Tab label="Sprints" id="project-tab-0" aria-controls="project-tabpanel-0"/>
              <Tab label="Epics" id="project-tab-1" aria-controls="project-tabpanel-1"/>
              <Tab label="Team" id="project-tab-2" aria-controls="project-tabpanel-2"/>
            </Tabs>
          </Box>
          <TabPanel value={tabValue} index={0}>
            <Box display="flex" justifyContent="space-between" alignItems="center" mb={2}>
              <Typography variant="h6">Sprints</Typography>
              <Button
                  component={RouterLink}
                  to={`/projects/${project.projectId}/sprints/create`}
                  variant="contained"
                  color="primary"
              >
                Create Sprint
              </Button>
            </Box>
            <Typography>Sprint list will be implemented here</Typography>
          </TabPanel>
          <TabPanel value={tabValue} index={1}>
            <Box display="flex" justifyContent="space-between" alignItems="center" mb={2}>
              <Typography variant="h6">Epics</Typography>
              <Button
                  component={RouterLink}
                  to={`/projects/${project.projectId}/epics/create`}
                  variant="contained"
                  color="primary"
              >
                Create Epic
              </Button>
            </Box>
            <Typography>Epic list will be implemented here</Typography>
          </TabPanel>
          <TabPanel value={tabValue} index={2}>
            <Box display="flex" justifyContent="space-between" alignItems="center" mb={2}>
              <Typography variant="h6">Team Members</Typography>
              <Button
                  component={RouterLink}
                  to={`/projects/${project.projectId}/team/manage`}
                  variant="contained"
                  color="primary"
              >
                Manage Team
              </Button>
            </Box>
            <Typography>Team member list will be implemented here</Typography>
          </TabPanel>
        </Box>
      </Container>
  );
};

export default ProjectDetail;
