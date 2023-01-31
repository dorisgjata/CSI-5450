import { Box } from '@mui/material';
import { Router } from './features/router/Router';

function App() {
  return (
    <div className="App">
      <header className="App-header">
        <Box sx={{ mx: 2, my: 1 }} >
          <Router />
        </Box>
      </header>
    </div>
  );
}

export default App;
