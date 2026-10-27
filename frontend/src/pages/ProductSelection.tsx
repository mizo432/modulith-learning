import React, {useEffect, useState} from 'react';
import {
  Box,
  Card,
  CardActionArea,
  CardContent,
  CardHeader,
  CircularProgress,
  Grid,
  InputAdornment,
  Paper,
  TextField,
  Typography
} from '@mui/material';
import {Search as SearchIcon} from '@mui/icons-material';
import {useNavigate} from 'react-router-dom';
import productService, {Product} from '../services/productService';

const ProductSelection: React.FC = () => {
  const [products, setProducts] = useState<Product[]>([]);
  const [filteredProducts, setFilteredProducts] = useState<Product[]>([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const data = await productService.getAllProducts();
        setProducts(data);
        setFilteredProducts(data);
        setLoading(false);
      } catch (err) {
        console.error('Error fetching products:', err);
        setError('Failed to load products. Please try again later.');
        setLoading(false);
      }
    };

    fetchProducts();
  }, []);

  useEffect(() => {
    if (searchTerm.trim() === '') {
      setFilteredProducts(products);
    } else {
      const filtered = products.filter(product =>
          product.name.toLowerCase().includes(searchTerm.toLowerCase())
      );
      setFilteredProducts(filtered);
    }
  }, [searchTerm, products]);

  const handleProductSelect = (productId: string) => {
    // Navigate to the selected product's dashboard or details page
    navigate(`/products/${productId}`);
  };

  const handleSearchChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setSearchTerm(event.target.value);
  };

  if (loading) {
    return (
        <Box display="flex" justifyContent="center" alignItems="center" minHeight="80vh">
          <CircularProgress/>
        </Box>
    );
  }

  if (error) {
    return (
        <Box>
          <Typography variant="h4" component="h1" gutterBottom>
            Product Selection
          </Typography>
          <Paper sx={{p: 3, mb: 4, bgcolor: '#ffebee'}}>
            <Typography variant="body1" color="error">
              {error}
            </Typography>
          </Paper>
        </Box>
    );
  }

  return (
      <Box>
        <Typography variant="h4" component="h1" gutterBottom>
          Product Selection
        </Typography>

        <Paper sx={{p: 3, mb: 4}}>
          <Typography variant="h6" gutterBottom>
            Select a product to work with
          </Typography>
          <Typography variant="body1" paragraph>
            Choose one of the available products below to view its details, backlog, and sprints.
          </Typography>

          <TextField
              fullWidth
              variant="outlined"
              placeholder="Search products..."
              value={searchTerm}
              onChange={handleSearchChange}
              sx={{mb: 3}}
              InputProps={{
                startAdornment: (
                    <InputAdornment position="start">
                      <SearchIcon/>
                    </InputAdornment>
                ),
              }}
          />
        </Paper>

        {filteredProducts.length === 0 ? (
            <Paper sx={{p: 3}}>
              <Typography variant="body1">
                No products
                found. {searchTerm ? 'Try a different search term.' : 'Create a new product to get started.'}
              </Typography>
            </Paper>
        ) : (
            <Grid container spacing={3}>
              {filteredProducts.map((product) => (
                  <Grid item xs={12} sm={6} md={4} key={product.productId}>
                    <Card>
                      <CardActionArea onClick={() => handleProductSelect(product.productId)}>
                        <CardHeader
                            title={product.name}
                            subheader={`Owner: ${product.productOwner.name}`}
                        />
                        <CardContent>
                          {product.vision && (
                              <Typography variant="body2" color="text.secondary" gutterBottom>
                                <strong>Vision:</strong> {product.vision}
                              </Typography>
                          )}
                          {product.description && (
                              <Typography variant="body2" color="text.secondary" noWrap>
                                {product.description}
                              </Typography>
                          )}
                        </CardContent>
                      </CardActionArea>
                    </Card>
                  </Grid>
              ))}
            </Grid>
        )}
      </Box>
  );
};

export default ProductSelection;
