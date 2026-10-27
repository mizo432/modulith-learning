import {createTheme} from '@mui/material/styles';

// Create a theme instance
const theme = createTheme({
  palette: {
    primary: {
      main: process.env.REACT_APP_PRIMARY_COLOR || '#1976d2',
    },
    secondary: {
      main: process.env.REACT_APP_SECONDARY_COLOR || '#dc004e',
    },
    background: {
      default: process.env.REACT_APP_BACKGROUND_COLOR || '#f5f5f5',
    },
  },
  typography: {
    fontFamily: [
      'Roboto',
      '"Helvetica Neue"',
      'Arial',
      'sans-serif',
    ].join(','),
  },
  components: {
    MuiButton: {
      styleOverrides: {
        root: {
          textTransform: 'none',
        },
      },
    },
  },
});

export default theme;
