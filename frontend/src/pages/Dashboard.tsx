import React from 'react';
import { Typography, Paper, Grid, Card, CardContent, CardHeader, Box } from '@mui/material';

const Dashboard: React.FC = () => {
  return (
    <Box>
      <Typography variant="h4" component="h1" gutterBottom>
        Dashboard
      </Typography>
      
      <Paper sx={{ p: 3, mb: 4 }}>
        <Typography variant="h6" gutterBottom>
          Welcome to Modulith Learning
        </Typography>
        <Typography variant="body1">
          This is a demonstration project for learning Spring Modulith architecture.
          The frontend is built with React and Material-UI.
        </Typography>
      </Paper>
      
      <Grid container spacing={3}>
        <Grid item xs={12} md={6}>
          <Card>
            <CardHeader title="Projects" />
            <CardContent>
              <Typography variant="body1">
                Manage your Scrum projects here.
              </Typography>
            </CardContent>
          </Card>
        </Grid>
        
        <Grid item xs={12} md={6}>
          <Card>
            <CardHeader title="Teams" />
            <CardContent>
              <Typography variant="body1">
                Manage your development teams here.
              </Typography>
            </CardContent>
          </Card>
        </Grid>
      </Grid>
    </Box>
  );
};

export default Dashboard;
