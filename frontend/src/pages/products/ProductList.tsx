import React, {useEffect, useState} from 'react';
import {
  Box,
  Button,
  Chip,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  IconButton,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Typography
} from '@mui/material';
import {Add as AddIcon, Archive as ArchiveIcon, Delete as DeleteIcon, Edit as EditIcon} from '@mui/icons-material';
import {Link as RouterLink} from 'react-router-dom';
import productService, {Product} from '../../services/productService';

const ProductList: React.FC = () => {
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);
  const [archiveDialogOpen, setArchiveDialogOpen] = useState(false);
  const [productToAction, setProductToAction] = useState<Product | null>(null);
  const [successMessage, setSuccessMessage] = useState<string | null>(null);

  // Load products on component mount
  useEffect(() => {
    fetchProducts();
  }, []);

  const fetchProducts = async () => {
    try {
      setLoading(true);
      const data = await productService.getAllProducts();
      setProducts(data);
      setError(null);
    } catch (err) {
      console.error('Failed to fetch products:', err);
      setError('プロダクト情報の取得に失敗しました。');
    } finally {
      setLoading(false);
    }
  };

  // Handle opening delete confirmation dialog
  const handleDeleteClick = (product: Product) => {
    setProductToAction(product);
    setDeleteDialogOpen(true);
  };

  // Handle opening archive confirmation dialog
  const handleArchiveClick = (product: Product) => {
    setProductToAction(product);
    setArchiveDialogOpen(true);
  };

  // Handle delete confirmation
  const handleDeleteConfirm = async () => {
    if (!productToAction) return;

    try {
      await productService.deleteProduct(productToAction.id);
      setProducts(products.filter(p => p.id !== productToAction.id));
      setSuccessMessage('プロダクトが正常に削除されました。');
      setError(null);
    } catch (err) {
      console.error('Failed to delete product:', err);
      setError('プロダクトの削除に失敗しました。');
    } finally {
      setDeleteDialogOpen(false);
      setProductToAction(null);
      setTimeout(() => setSuccessMessage(null), 3000);
    }
  };

  // Handle archive confirmation
  const handleArchiveConfirm = async () => {
    if (!productToAction) return;

    try {
      await productService.archiveProduct(productToAction.id);
      setProducts(products.map(p =>
          p.id === productToAction.id ? {...p, status: 'ARCHIVED' as const} : p
      ));
      setSuccessMessage('プロダクトが正常にアーカイブされました。');
      setError(null);
    } catch (err) {
      console.error('Failed to archive product:', err);
      setError('プロダクトのアーカイブに失敗しました。');
    } finally {
      setArchiveDialogOpen(false);
      setProductToAction(null);
      setTimeout(() => setSuccessMessage(null), 3000);
    }
  };

  // Handle dialog cancellation
  const handleCancel = () => {
    setDeleteDialogOpen(false);
    setArchiveDialogOpen(false);
    setProductToAction(null);
  };

  // Get status display
  const getStatusDisplay = (status: string) => {
    switch (status) {
      case 'ACTIVE':
        return {label: '有効', color: 'success' as const};
      case 'ARCHIVED':
        return {label: 'アーカイブ', color: 'default' as const};
      default:
        return {label: status, color: 'default' as const};
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

  return (
      <Box>
        <Box sx={{display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3}}>
          <Typography variant="h4" component="h1">
            プロダクト管理
          </Typography>
          <Button
              variant="contained"
              color="primary"
              startIcon={<AddIcon/>}
              component={RouterLink}
              to="/products/add"
          >
            プロダクト追加
          </Button>
        </Box>

        {successMessage && (
            <Paper sx={{p: 2, mb: 3, bgcolor: 'success.light', color: 'success.contrastText'}}>
              <Typography>{successMessage}</Typography>
            </Paper>
        )}

        {error && (
            <Paper sx={{p: 2, mb: 3, bgcolor: 'error.light', color: 'error.contrastText'}}>
              <Typography>{error}</Typography>
            </Paper>
        )}

        <TableContainer component={Paper}>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>名前</TableCell>
                <TableCell>説明</TableCell>
                <TableCell>マネージャーID</TableCell>
                <TableCell>ステータス</TableCell>
                <TableCell>作成日時</TableCell>
                <TableCell>更新日時</TableCell>
                <TableCell>アクション</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {loading ? (
                  <TableRow>
                    <TableCell colSpan={7} align="center">読み込み中...</TableCell>
                  </TableRow>
              ) : products.length === 0 ? (
                  <TableRow>
                    <TableCell colSpan={7} align="center">プロダクトが見つかりません</TableCell>
                  </TableRow>
              ) : (
                  products.map((product) => {
                    const statusInfo = getStatusDisplay(product.status);
                    return (
                        <TableRow key={product.id}>
                          <TableCell>{product.name}</TableCell>
                          <TableCell>
                            {product.description ? (
                                product.description.length > 50
                                    ? `${product.description.substring(0, 50)}...`
                                    : product.description
                            ) : '-'}
                          </TableCell>
                          <TableCell>{product.managerId || '-'}</TableCell>
                          <TableCell>
                            <Chip
                                label={statusInfo.label}
                                color={statusInfo.color}
                                size="small"
                            />
                          </TableCell>
                          <TableCell>{formatDate(product.createdAt)}</TableCell>
                          <TableCell>{formatDate(product.updatedAt)}</TableCell>
                          <TableCell>
                            <IconButton
                                component={RouterLink}
                                to={`/products/edit/${product.id}`}
                                color="primary"
                                size="small"
                            >
                              <EditIcon/>
                            </IconButton>
                            {product.status === 'ACTIVE' && (
                                <IconButton
                                    onClick={() => handleArchiveClick(product)}
                                    color="warning"
                                    size="small"
                                    title="アーカイブ"
                                >
                                  <ArchiveIcon/>
                                </IconButton>
                            )}
                            <IconButton
                                onClick={() => handleDeleteClick(product)}
                                color="error"
                                size="small"
                            >
                              <DeleteIcon/>
                            </IconButton>
                          </TableCell>
                        </TableRow>
                    );
                  })
              )}
            </TableBody>
          </Table>
        </TableContainer>

        {/* Delete Confirmation Dialog */}
        <Dialog
            open={deleteDialogOpen}
            onClose={handleCancel}
        >
          <DialogTitle>プロダクト削除の確認</DialogTitle>
          <DialogContent>
            <DialogContentText>
              {productToAction && `プロダクト「${productToAction.name}」を削除してもよろしいですか？`}
              この操作は元に戻せません。
            </DialogContentText>
          </DialogContent>
          <DialogActions>
            <Button onClick={handleCancel} color="primary">
              キャンセル
            </Button>
            <Button onClick={handleDeleteConfirm} color="error">
              削除
            </Button>
          </DialogActions>
        </Dialog>

        {/* Archive Confirmation Dialog */}
        <Dialog
            open={archiveDialogOpen}
            onClose={handleCancel}
        >
          <DialogTitle>プロダクトアーカイブの確認</DialogTitle>
          <DialogContent>
            <DialogContentText>
              {productToAction && `プロダクト「${productToAction.name}」をアーカイブしてもよろしいですか？`}
              アーカイブされたプロダクトは編集できなくなります。
            </DialogContentText>
          </DialogContent>
          <DialogActions>
            <Button onClick={handleCancel} color="primary">
              キャンセル
            </Button>
            <Button onClick={handleArchiveConfirm} color="warning">
              アーカイブ
            </Button>
          </DialogActions>
        </Dialog>
      </Box>
  );
};

export default ProductList;
