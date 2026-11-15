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

  const handleProductSelect = (productId: number) => {
    // Navigate to the selected product's projects page
    navigate(`/projects?productId=${productId}`);
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
          製品選択
        </Typography>

        <Paper sx={{p: 3, mb: 4}}>
          <Typography variant="h6" gutterBottom>
            作業する製品を選択してください
          </Typography>
          <Typography variant="body1" paragraph>
            下の製品から選択して、詳細、バックログ、スプリントを確認できます。
          </Typography>

          <TextField
              fullWidth
              variant="outlined"
              placeholder="製品を検索..."
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
                製品が見つかりません。{searchTerm ? ' 別の検索語句を試してください。' : ' 新しい製品を作成してください。'}
              </Typography>
            </Paper>
        ) : (
            <Grid container spacing={3}>
              {filteredProducts.map((product) => (
                  <Grid item xs={12} sm={6} md={4} key={product.id}>
                    <Card>
                      <CardActionArea onClick={() => handleProductSelect(product.id)}>
                        <CardHeader
                            title={product.name}
                            subheader={product.managerId ? `マネージャーID: ${product.managerId}` : 'マネージャー未設定'}
                        />
                        <CardContent>
                          {product.description && (
                              <Typography variant="body2" color="text.secondary" noWrap>
                                {product.description}
                              </Typography>
                          )}
                          <Typography variant="caption" color="text.secondary" sx={{mt: 1, display: 'block'}}>
                            ステータス: {product.status === 'ACTIVE' ? '有効' : 'アーカイブ'}
                          </Typography>
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
