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
import {Add as AddIcon, Delete as DeleteIcon, Edit as EditIcon} from '@mui/icons-material';
import {Link as RouterLink} from 'react-router-dom';
import userService, {User} from '../../services/userService';

const UserList: React.FC = () => {
  const [users, setUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);
  const [userToDelete, setUserToDelete] = useState<User | null>(null);

  // Load users on component mount
  useEffect(() => {
    const fetchUsers = async () => {
      try {
        setLoading(true);
        const data = await userService.getAllUsers();
        setUsers(data);
        setError(null);
      } catch (err) {
        console.error('Failed to fetch users:', err);
        setError('ユーザー情報の取得に失敗しました。');
      } finally {
        setLoading(false);
      }
    };

    fetchUsers();
  }, []);

  // Handle opening delete confirmation dialog
  const handleDeleteClick = (user: User) => {
    setUserToDelete(user);
    setDeleteDialogOpen(true);
  };

  // Handle delete confirmation
  const handleDeleteConfirm = async () => {
    if (!userToDelete) return;

    try {
      await userService.deleteUser(userToDelete.id);
      // Remove the deleted user from the state
      setUsers(users.filter(user => user.id !== userToDelete.id));
      setError(null);
    } catch (err) {
      console.error('Failed to delete user:', err);
      setError('ユーザーの削除に失敗しました。');
    } finally {
      setDeleteDialogOpen(false);
      setUserToDelete(null);
    }
  };

  // Handle delete cancellation
  const handleDeleteCancel = () => {
    setDeleteDialogOpen(false);
    setUserToDelete(null);
  };

  // Get user type display text
  const getUserTypeDisplay = (userType: string) => {
    switch (userType) {
      case 'EMPLOYEE':
        return '社員';
      case 'BUSINESS_PARTNER_EMPLOYEE':
        return 'ビジネスパートナー社員';
      case 'INDIVIDUAL_BUSINESS_PARTNER':
        return 'ビジネスパートナー（個人）';
      default:
        return userType;
    }
  };

  return (
      <Box>
        <Box sx={{display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3}}>
          <Typography variant="h4" component="h1">
            ユーザー管理
          </Typography>
          <Button
              variant="contained"
              color="primary"
              startIcon={<AddIcon/>}
              component={RouterLink}
              to="/users/add"
          >
            ユーザー追加
          </Button>
        </Box>

        {error && (
            <Paper sx={{p: 2, mb: 3, bgcolor: 'error.light', color: 'error.contrastText'}}>
              <Typography>{error}</Typography>
            </Paper>
        )}

        <TableContainer component={Paper}>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>ユーザー名</TableCell>
                <TableCell>名前</TableCell>
                <TableCell>メールアドレス</TableCell>
                <TableCell>ユーザータイプ</TableCell>
                <TableCell>ロール</TableCell>
                <TableCell>ステータス</TableCell>
                <TableCell>アクション</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {loading ? (
                  <TableRow>
                    <TableCell colSpan={7} align="center">読み込み中...</TableCell>
                  </TableRow>
              ) : users.length === 0 ? (
                  <TableRow>
                    <TableCell colSpan={7} align="center">ユーザーが見つかりません</TableCell>
                  </TableRow>
              ) : (
                  users.map((user) => (
                      <TableRow key={user.id}>
                        <TableCell>{user.username}</TableCell>
                        <TableCell>
                          {user.lastName} {user.firstName}
                          {user.initials && <span> ({user.initials})</span>}
                        </TableCell>
                        <TableCell>{user.email}</TableCell>
                        <TableCell>{getUserTypeDisplay(user.userType)}</TableCell>
                        <TableCell>
                          {user.roles.map(role => (
                              <Chip
                                  key={role.id}
                                  label={role.name}
                                  size="small"
                                  sx={{mr: 0.5, mb: 0.5}}
                              />
                          ))}
                        </TableCell>
                        <TableCell>
                          <Chip
                              label={user.enabled ? '有効' : '無効'}
                              color={user.enabled ? 'success' : 'error'}
                              size="small"
                          />
                        </TableCell>
                        <TableCell>
                          <IconButton
                              component={RouterLink}
                              to={`/users/edit/${user.id}`}
                              color="primary"
                              size="small"
                          >
                            <EditIcon/>
                          </IconButton>
                          <IconButton
                              onClick={() => handleDeleteClick(user)}
                              color="error"
                              size="small"
                          >
                            <DeleteIcon/>
                          </IconButton>
                        </TableCell>
                      </TableRow>
                  ))
              )}
            </TableBody>
          </Table>
        </TableContainer>

        {/* Delete Confirmation Dialog */}
        <Dialog
            open={deleteDialogOpen}
            onClose={handleDeleteCancel}
        >
          <DialogTitle>ユーザー削除の確認</DialogTitle>
          <DialogContent>
            <DialogContentText>
              {userToDelete && `ユーザー「${userToDelete.username}」を削除してもよろしいですか？`}
              この操作は元に戻せません。
            </DialogContentText>
          </DialogContent>
          <DialogActions>
            <Button onClick={handleDeleteCancel} color="primary">
              キャンセル
            </Button>
            <Button onClick={handleDeleteConfirm} color="error">
              削除
            </Button>
          </DialogActions>
        </Dialog>
      </Box>
  );
};

export default UserList;
