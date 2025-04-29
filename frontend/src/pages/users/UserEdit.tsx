import React, {useEffect, useState} from 'react';
import {
  Alert,
  Box,
  Button,
  Chip,
  FormControl,
  FormControlLabel,
  FormHelperText,
  Grid,
  InputLabel,
  MenuItem,
  OutlinedInput,
  Paper,
  Select,
  SelectChangeEvent,
  Switch,
  TextField,
  Typography
} from '@mui/material';
import {useNavigate, useParams} from 'react-router-dom';
import userService, {User, UserAdminUpdateRequest} from '../../services/userService';

// Available user types
const userTypes = [
  {value: 'EMPLOYEE', label: '社員'},
  {value: 'BUSINESS_PARTNER_EMPLOYEE', label: 'ビジネスパートナー社員'},
  {value: 'INDIVIDUAL_BUSINESS_PARTNER', label: 'ビジネスパートナー（個人）'}
];

interface Role {
  id: number;
  name: string;
  description?: string;
}

const UserEdit: React.FC = () => {
  const navigate = useNavigate();
  const {id} = useParams<{ id: string }>();
  const [user, setUser] = useState<User | null>(null);
  const [formData, setFormData] = useState<UserAdminUpdateRequest>({
    username: '',
    email: '',
    firstName: '',
    lastName: '',
    initials: '',
    userType: undefined,
    enabled: true,
    password: '',
    roles: []
  });

  const [errors, setErrors] = useState<Record<string, string>>({});
  const [submitError, setSubmitError] = useState<string | null>(null);
  const [availableRoles, setAvailableRoles] = useState<Role[]>([]);
  const [loading, setLoading] = useState(false);
  const [fetchLoading, setFetchLoading] = useState(true);

  // Fetch user data and available roles on component mount
  useEffect(() => {
    const fetchData = async () => {
      try {
        setFetchLoading(true);

        // Fetch user data
        if (id) {
          const userData = await userService.getUserById(parseInt(id, 10));
          setUser(userData);

          // Initialize form data with user data
          setFormData({
            username: userData.username,
            email: userData.email,
            firstName: userData.firstName || '',
            lastName: userData.lastName || '',
            initials: userData.initials || '',
            userType: userData.userType,
            enabled: userData.enabled,
            password: '', // Password is not included in the response
            roles: userData.roles.map(role => role.name)
          });
        }

        // Fetch available roles (placeholder)
        setAvailableRoles([
          {id: 1, name: 'ADMIN', description: '管理者'},
          {id: 2, name: 'USER', description: '一般ユーザー'},
          {id: 3, name: 'MANAGER', description: 'マネージャー'}
        ]);

        setSubmitError(null);
      } catch (err) {
        console.error('Failed to fetch data:', err);
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

  // Handle user type selection
  const handleUserTypeChange = (e: SelectChangeEvent<string>) => {
    setFormData(prev => ({...prev, userType: e.target.value as any}));
  };

  // Handle roles selection
  const handleRolesChange = (e: SelectChangeEvent<string[]>) => {
    const value = e.target.value;
    setFormData(prev => ({
      ...prev,
      roles: typeof value === 'string' ? value.split(',') : value
    }));
  };

  // Handle enabled toggle
  const handleEnabledChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData(prev => ({...prev, enabled: e.target.checked}));
  };

  // Validate form data
  const validateForm = (): boolean => {
    const newErrors: Record<string, string> = {};

    if (!formData.username) {
      newErrors.username = 'ユーザー名は必須です';
    }

    if (formData.password && formData.password.length < 8) {
      newErrors.password = 'パスワードは8文字以上である必要があります';
    }

    if (!formData.email) {
      newErrors.email = 'メールアドレスは必須です';
    } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
      newErrors.email = '有効なメールアドレスを入力してください';
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

    // Remove empty password if not changed
    const updateData = {...formData};
    if (!updateData.password) {
      delete updateData.password;
    }

    try {
      await userService.updateUser(parseInt(id, 10), updateData);
      navigate('/users/all', {state: {message: 'ユーザーが正常に更新されました'}});
    } catch (err) {
      console.error('Failed to update user:', err);
      setSubmitError('ユーザーの更新に失敗しました。');
    } finally {
      setLoading(false);
    }
  };

  if (fetchLoading) {
    return (
        <Box>
          <Typography variant="h4" component="h1" gutterBottom>
            ユーザー編集
          </Typography>
          <Paper sx={{p: 3}}>
            <Typography>読み込み中...</Typography>
          </Paper>
        </Box>
    );
  }

  if (!user && !fetchLoading) {
    return (
        <Box>
          <Typography variant="h4" component="h1" gutterBottom>
            ユーザー編集
          </Typography>
          <Paper sx={{p: 3}}>
            <Typography color="error">ユーザーが見つかりません</Typography>
            <Button
                variant="contained"
                onClick={() => navigate('/users/all')}
                sx={{mt: 2}}
            >
              ユーザー一覧に戻る
            </Button>
          </Paper>
        </Box>
    );
  }

  return (
      <Box>
        <Typography variant="h4" component="h1" gutterBottom>
          ユーザー編集: {user?.username}
        </Typography>

        {submitError && (
            <Alert severity="error" sx={{mb: 3}}>
              {submitError}
            </Alert>
        )}

        <Paper sx={{p: 3}}>
          <form onSubmit={handleSubmit}>
            <Grid container spacing={3}>
              <Grid item xs={12} md={6}>
                <TextField
                    fullWidth
                    label="ユーザー名"
                    name="username"
                    value={formData.username}
                    onChange={handleChange}
                    error={!!errors.username}
                    helperText={errors.username}
                    required
                    disabled={loading}
                />
              </Grid>

              <Grid item xs={12} md={6}>
                <TextField
                    fullWidth
                    label="パスワード (変更する場合のみ入力)"
                    name="password"
                    type="password"
                    value={formData.password}
                    onChange={handleChange}
                    error={!!errors.password}
                    helperText={errors.password || 'パスワードを変更しない場合は空白のままにしてください'}
                    disabled={loading}
                />
              </Grid>

              <Grid item xs={12} md={6}>
                <TextField
                    fullWidth
                    label="メールアドレス"
                    name="email"
                    type="email"
                    value={formData.email}
                    onChange={handleChange}
                    error={!!errors.email}
                    helperText={errors.email}
                    required
                    disabled={loading}
                />
              </Grid>

              <Grid item xs={12} md={6}>
                <FormControl fullWidth disabled={loading}>
                  <InputLabel id="user-type-label">ユーザータイプ</InputLabel>
                  <Select
                      labelId="user-type-label"
                      value={formData.userType || ''}
                      onChange={handleUserTypeChange}
                      label="ユーザータイプ"
                  >
                    {userTypes.map(type => (
                        <MenuItem key={type.value} value={type.value}>
                          {type.label}
                        </MenuItem>
                    ))}
                  </Select>
                </FormControl>
              </Grid>

              <Grid item xs={12} md={4}>
                <TextField
                    fullWidth
                    label="姓"
                    name="lastName"
                    value={formData.lastName || ''}
                    onChange={handleChange}
                    disabled={loading}
                />
              </Grid>

              <Grid item xs={12} md={4}>
                <TextField
                    fullWidth
                    label="名"
                    name="firstName"
                    value={formData.firstName || ''}
                    onChange={handleChange}
                    disabled={loading}
                />
              </Grid>

              <Grid item xs={12} md={4}>
                <TextField
                    fullWidth
                    label="イニシャル"
                    name="initials"
                    value={formData.initials || ''}
                    onChange={handleChange}
                    disabled={loading}
                />
              </Grid>

              <Grid item xs={12} md={6}>
                <FormControlLabel
                    control={
                      <Switch
                          checked={formData.enabled}
                          onChange={handleEnabledChange}
                          name="enabled"
                          color="primary"
                          disabled={loading}
                      />
                    }
                    label="アカウント有効"
                />
              </Grid>

              <Grid item xs={12}>
                <FormControl fullWidth disabled={loading}>
                  <InputLabel id="roles-label">ロール</InputLabel>
                  <Select
                      labelId="roles-label"
                      multiple
                      value={formData.roles || []}
                      onChange={handleRolesChange}
                      input={<OutlinedInput label="ロール"/>}
                      renderValue={(selected) => (
                          <Box sx={{display: 'flex', flexWrap: 'wrap', gap: 0.5}}>
                            {selected.map((value) => (
                                <Chip key={value} label={value}/>
                            ))}
                          </Box>
                      )}
                  >
                    {availableRoles.map((role) => (
                        <MenuItem key={role.id} value={role.name}>
                          {role.name} {role.description && `- ${role.description}`}
                        </MenuItem>
                    ))}
                  </Select>
                  <FormHelperText>ユーザーに割り当てるロールを選択してください</FormHelperText>
                </FormControl>
              </Grid>

              <Grid item xs={12} sx={{mt: 2, display: 'flex', justifyContent: 'flex-end'}}>
                <Button
                    type="button"
                    onClick={() => navigate('/users/all')}
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

export default UserEdit;
