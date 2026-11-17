import React, {useState} from 'react';
import {
  Alert,
  Box,
  Button,
  Grid,
  Paper,
  TextField,
  Typography
} from '@mui/material';
import {useNavigate} from 'react-router-dom';
import productService, {ProductCreateRequest} from '../../services/productService';

const ProductCreate: React.FC = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState<ProductCreateRequest>({
    name: '',
    description: '',
    managerId: ''
  });

  const [errors, setErrors] = useState<Record<string, string>>({});
  const [submitError, setSubmitError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  // Handle form field changes
  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const {name, value} = e.target;
    setFormData(prev => ({...prev, [name]: value}));

    // Clear error for this field
    if (errors[name]) {
      setErrors(prev => {
        const newErrors = {...prev};
        delete newErrors[name];
        return newErrors;
      });
    }
  };

  // Validate form data
  const validateForm = (): boolean => {
    const newErrors: Record<string, string> = {};

    if (!formData.name || formData.name.trim() === '') {
      newErrors.name = 'プロダクト名は必須です';
    } else if (formData.name.length > 100) {
      newErrors.name = 'プロダクト名は100文字以内で入力してください';
    }

    if (formData.managerId && !/^[0-9]+$/.test(formData.managerId)) {
      newErrors.managerId = '有効な数値ID形式で入力してください';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  // Handle form submission
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!validateForm()) {
      return;
    }

    setLoading(true);
    setSubmitError(null);

    try {
      // Build request - only include managerId if it's provided
      const request: ProductCreateRequest = {
        name: formData.name.trim(),
        description: formData.description?.trim() || undefined
      };
      if (formData.managerId && formData.managerId.trim()) {
        request.managerId = formData.managerId.trim();
      }

      await productService.createProduct(request);
      navigate('/products/all', {state: {message: 'プロダクトが正常に作成されました'}});
    } catch (err) {
      console.error('Failed to create product:', err);
      setSubmitError('プロダクトの作成に失敗しました。');
    } finally {
      setLoading(false);
    }
  };

  return (
      <Box>
        <Typography variant="h4" component="h1" gutterBottom>
          プロダクト追加
        </Typography>

        {submitError && (
            <Alert severity="error" sx={{mb: 3}}>
              {submitError}
            </Alert>
        )}

        <Paper sx={{p: 3}}>
          <form onSubmit={handleSubmit}>
            <Grid container spacing={3}>
              <Grid item xs={12}>
                <TextField
                    fullWidth
                    label="プロダクト名"
                    name="name"
                    value={formData.name}
                    onChange={handleChange}
                    error={!!errors.name}
                    helperText={errors.name || 'プロダクトの名前を入力してください（必須）'}
                    required
                    disabled={loading}
                    multiline
                    maxRows={3}
                />
              </Grid>

              <Grid item xs={12}>
                <TextField
                    fullWidth
                    label="説明"
                    name="description"
                    value={formData.description}
                    onChange={handleChange}
                    error={!!errors.description}
                    helperText={errors.description || 'プロダクトの説明をMarkdown形式で入力してください（任意）'}
                    disabled={loading}
                    multiline
                    rows={6}
                />
              </Grid>

              <Grid item xs={12} md={6}>
                <TextField
                    fullWidth
                    label="マネージャーID"
                    name="managerId"
                    value={formData.managerId}
                    onChange={handleChange}
                    error={!!errors.managerId}
                    helperText={errors.managerId || 'マネージャーのID（任意）'}
                    disabled={loading}
                />
              </Grid>

              <Grid item xs={12} sx={{mt: 2, display: 'flex', justifyContent: 'flex-end'}}>
                <Button
                    type="button"
                    onClick={() => navigate('/products/all')}
                    sx={{mr: 2}}
                    disabled={loading}
                >
                  キャンセル
                </Button>
                <Button
                    type="submit"
                    variant="contained"
                    color="primary"
                    disabled={loading}
                >
                  {loading ? '保存中...' : '保存'}
                </Button>
              </Grid>
            </Grid>
          </form>
        </Paper>
      </Box>
  );
};

export default ProductCreate;
