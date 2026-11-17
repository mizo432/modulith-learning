import React, {useState} from 'react';
import {useNavigate} from 'react-router-dom';
import {Alert, Box, Button, Container, Paper, TextField, Typography} from '@mui/material';
import api from '../services/api';
import {useAuth} from '../contexts/AuthContext';

const ChangePassword: React.FC = () => {
  const navigate = useNavigate();
  const {setFirstLogin} = useAuth();
  const [newPassword, setNewPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');

    // バリデーション
    if (newPassword.length < 8) {
      setError('パスワードは8文字以上で入力してください');
      return;
    }
    if (newPassword.length > 64) {
      setError('パスワードは64文字以下で入力してください');
      return;
    }
    if (newPassword !== confirmPassword) {
      setError('パスワードと確認用パスワードが一致しません');
      return;
    }

    setLoading(true);

    try {
      const response = await api.post('/auth/first-login/change-password', {
        newPassword,
        confirmPassword,
      });

      if (response.data.success) {
        // 初回ログインフラグをクリア
        setFirstLogin(false);
        // ダッシュボードにリダイレクト
        navigate('/');
      } else {
        setError(response.data.message || 'パスワードの変更に失敗しました');
      }
    } catch (err: any) {
      setError(err.response?.data?.message || 'エラーが発生しました');
    } finally {
      setLoading(false);
    }
  };

  return (
      <Container maxWidth="sm">
        <Box sx={{mt: 8}}>
          <Paper elevation={3} sx={{p: 4}}>
            <Typography variant="h4" component="h1" align="center" gutterBottom>
              Change Password
            </Typography>
            <Typography variant="body2" color="text.secondary" align="center" paragraph>
              初回ログインです。新しいパスワードを設定してください。
            </Typography>

            {error && (
                <Alert severity="error" sx={{mb: 2}}>
                  {error}
                </Alert>
            )}

            <Box component="form" onSubmit={handleSubmit} noValidate>
              <TextField
                  margin="normal"
                  required
                  fullWidth
                  name="newPassword"
                  label="New Password"
                  type="password"
                  id="newPassword"
                  autoComplete="new-password"
                  value={newPassword}
                  onChange={(e) => setNewPassword(e.target.value)}
              />
              <TextField
                  margin="normal"
                  required
                  fullWidth
                  name="confirmPassword"
                  label="Confirm Password"
                  type="password"
                  id="confirmPassword"
                  autoComplete="new-password"
                  value={confirmPassword}
                  onChange={(e) => setConfirmPassword(e.target.value)}
              />
              <Button
                  type="submit"
                  fullWidth
                  variant="contained"
                  sx={{mt: 3, mb: 2}}
                  disabled={loading}
              >
                {loading ? 'Changing...' : 'Change Password'}
              </Button>
            </Box>
          </Paper>
        </Box>
      </Container>
  );
};

export default ChangePassword;
