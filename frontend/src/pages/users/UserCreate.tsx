import React, {useEffect, useState} from 'react';
import {
  Alert,
  Box,
  Button,
  Chip,
  FormControl,
  FormHelperText,
  Grid,
  InputLabel,
  MenuItem,
  OutlinedInput,
  Paper,
  Select,
  SelectChangeEvent,
  TextField,
  Typography
} from '@mui/material';
import {useNavigate} from 'react-router-dom';
import userService, {UserCreateRequest} from '../../services/userService';

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

const UserCreate: React.FC = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState<UserCreateRequest>({
    username: '',
    password: '',
    email: '',
    firstName: '',
    lastName: '',
    initials: '',
    userType: 'EMPLOYEE',
    roles: []
  });

  const [errors, setErrors] = useState<Record<string, string>>({});
  const [submitError, setSubmitError] = useState<string | null>(null);
  const [availableRoles, setAvailableRoles] = useState<Role[]>([]);
  const [loading, setLoading] = useState(false);

  // Fetch available roles on component mount
  useEffect(() => {
    const fetchRoles = async () => {
      try {
        // This is a placeholder - you would need to implement a method to fetch roles
        // For now, we'll use some dummy data
        setAvailableRoles([
          {id: 1, name: 'ADMIN', description: '管理者'},
          {id: 2, name: 'USER', description: '一般ユーザー'},
          {id: 3, name: 'MANAGER', description: 'マネージャー'}
        ]);
      } catch (err) {
        console.error('Failed to fetch roles:', err);
        setSubmitError('ロール情報の取得に失敗しました。');
      }
    };

    fetchRoles();
  }, []);

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
    setFormData(prev => ({...prev, userType: e.target.value}));
  };

  // Handle roles selection
  const handleRolesChange = (e: SelectChangeEvent<string[]>) => {
    const value = e.target.value;
    setFormData(prev => ({
      ...prev,
      roles: typeof value === 'string' ? value.split(',') : value
    }));
  };

  // Validate form data
  const validateForm = (): boolean => {
    const newErrors: Record<string, string> = {};

    if (!formData.username) {
      newErrors.username = 'ユーザー名は必須です';
    }

    if (!formData.password) {
      newErrors.password = 'パスワードは必須です';
    } else if (formData.password.length < 8) {
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

    if (!validateForm()) {
      return;
    }

    setLoading(true);
    setSubmitError(null);

    try {
      await userService.createUser(formData);
      navigate('/users/all', {state: {message: 'ユーザーが正常に作成されました'}});
    } catch (err) {
      console.error('Failed to create user:', err);
      setSubmitError('ユーザーの作成に失敗しました。');
    } finally {
      setLoading(false);
    }
  };

  return (
      <Box>
        <Typography variant="h4" component="h1" gutterBottom>
          ユーザー追加
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
                    label="パスワード"
                    name="password"
                    type="password"
                    value={formData.password}
                    onChange={handleChange}
                    error={!!errors.password}
                    helperText={errors.password}
                    required
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

export default UserCreate;
