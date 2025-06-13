import React from 'react';
import {Box, Button, Card, CardContent, CardHeader, Grid, Paper, Typography} from '@mui/material';
import {useNavigate} from 'react-router-dom';

const Dashboard: React.FC = () => {
  const navigate = useNavigate();

  const handleGoToProducts = () => {
    navigate('/products');
  };

  return (
      <Box>
        <Typography variant="h4" component="h1" gutterBottom>
          Dashboard
        </Typography>

        <Paper sx={{p: 3, mb: 4}}>
          <Typography variant="h6" gutterBottom>
            Welcome to Modulith Learning
          </Typography>
          <Typography variant="body1" paragraph>
            This is a demonstration project for learning Spring Modulith architecture.
            The frontend is built with React and Material-UI.
          </Typography>
          <Button
              variant="contained"
              color="primary"
              onClick={handleGoToProducts}
              sx={{mt: 2}}
          >
            製品選択画面へ
          </Button>
        </Paper>

        <Grid container spacing={3}>
          <Grid item xs={12} md={6}>
            <Card>
              <CardHeader title="Projects"/>
              <CardContent>
                <Typography variant="body1" paragraph>
                  Manage your Scrum projects here.
                </Typography>
                <Button
                    variant="outlined"
                    color="primary"
                    onClick={handleGoToProducts}
                >
                  製品を選択
                </Button>
              </CardContent>
            </Card>
          </Grid>

          <Grid item xs={12} md={6}>
            <Card>
              <CardHeader title="Teams"/>
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
