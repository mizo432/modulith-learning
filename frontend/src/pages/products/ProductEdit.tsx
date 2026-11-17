import React, {useEffect, useState} from 'react';
import {
  Alert,
  Box,
  Button,
  Chip,
  Grid,
  Paper,
  TextField,
  Typography
} from '@mui/material';
import {useNavigate, useParams} from 'react-router-dom';
import productService, {Product, ProductUpdateRequest} from '../../services/productService';

const ProductEdit: React.FC = () => {
  const navigate = useNavigate();
  const {id} = useParams<{ id: string }>();
  const [product, setProduct] = useState<Product | null>(null);
  const [formData, setFormData] = useState<ProductUpdateRequest>({
    name: '',
    description: '',
    managerId: ''
  });

  const [errors, setErrors] = useState<Record<string, string>>({});
  const [submitError, setSubmitError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);
  const [fetchLoading, setFetchLoading] = useState(true);

  // Fetch product data on component mount
  useEffect(() => {
    const fetchData = async () => {
      try {
        setFetchLoading(true);

        if (id) {
          const productData = await productService.getProductById(parseInt(id, 10));
          setProduct(productData);

          // Initialize form data with product data
          setFormData({
            name: productData.name,
            description: productData.description || '',
            managerId: productData.managerId || ''
          });
        }

        setSubmitError(null);
      } catch (err) {
        console.error('Failed to fetch product:', err);
        setSubmitError('データの取得に失敗しました。');
      } finally {
        setFetchLoading(false);
      }
    };

    fetchData();
  }, [id]);

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

    if (!validateForm() || !id) {
      return;
    }

    setLoading(true);
    setSubmitError(null);

    try {
      // Build request with only changed fields
      const request: ProductUpdateRequest = {};
      if (formData.name?.trim()) {
        request.name = formData.name.trim();
      }
      if (formData.description !== undefined) {
        request.description = formData.description.trim() || undefined;
      }
      if (formData.managerId && formData.managerId.trim()) {
        request.managerId = formData.managerId.trim();
      } else if (formData.managerId === '') {
        // Clear managerId if field is empty
        request.managerId = undefined;
      }

      await productService.updateProduct(parseInt(id, 10), request);
      navigate('/products/all', {state: {message: 'プロダクトが正常に更新されました'}});
    } catch (err) {
      console.error('Failed to update product:', err);
      setSubmitError('プロダクトの更新に失敗しました。');
    } finally {
      setLoading(false);
    }
  };

  // Format date
  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    return date.toLocaleDateString('ja-JP', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  if (fetchLoading) {
    return (
        <Box>
          <Typography variant="h4" component="h1" gutterBottom>
            プロダクト編集
          </Typography>
          <Paper sx={{p: 3}}>
            <Typography>読み込み中...</Typography>
          </Paper>
        </Box>
    );
  }

  if (!product && !fetchLoading) {
    return (
        <Box>
          <Typography variant="h4" component="h1" gutterBottom>
            プロダクト編集
          </Typography>
          <Paper sx={{p: 3}}>
            <Typography color="error">プロダクトが見つかりません</Typography>
            <Button
                variant="contained"
                onClick={() => navigate('/products/all')}
                sx={{mt: 2}}
            >
              プロダクト一覧に戻る
            </Button>
          </Paper>
        </Box>
    );
  }

  // Check if product is archived
  const isArchived = product?.status === 'ARCHIVED';

  return (
      <Box>
        <Typography variant="h4" component="h1" gutterBottom>
          プロダクト編集: {product?.name}
        </Typography>

        {isArchived && (
            <Alert severity="warning" sx={{mb: 3}}>
              このプロダクトはアーカイブされています。編集はできません。
            </Alert>
        )}

        {submitError && (
            <Alert severity="error" sx={{mb: 3}}>
              {submitError}
            </Alert>
        )}

        <Paper sx={{p: 3, mb: 3}}>
          <Typography variant="subtitle1" color="text.secondary" gutterBottom>
            基本情報
          </Typography>
          <Grid container spacing={2}>
            <Grid item xs={12} sm={6}>
              <Typography variant="body2" color="text.secondary">ステータス</Typography>
              <Chip
                  label={product?.status === 'ACTIVE' ? '有効' : 'アーカイブ'}
                  color={product?.status === 'ACTIVE' ? 'success' : 'default'}
                  size="small"
                  sx={{mt: 0.5}}
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <Typography variant="body2" color="text.secondary">作成日時</Typography>
              <Typography variant="body2" sx={{mt: 0.5}}>
                {product?.createdAt ? formatDate(product.createdAt) : '-'}
              </Typography>
            </Grid>
            <Grid item xs={12} sm={6}>
              <Typography variant="body2" color="text.secondary">更新日時</Typography>
              <Typography variant="body2" sx={{mt: 0.5}}>
                {product?.updatedAt ? formatDate(product.updatedAt) : '-'}
              </Typography>
            </Grid>
          </Grid>
        </Paper>

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
                    disabled={loading || isArchived}
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
                    disabled={loading || isArchived}
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
                    disabled={loading || isArchived}
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
                    disabled={loading || isArchived}
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

export default ProductEdit;
