import React, {useEffect, useRef, useState, useCallback} from 'react';
import {useParams, Link as RouterLink} from 'react-router-dom';
import {
  Box,
  Button,
  Container,
  FormControl,
  FormHelperText,
  InputLabel,
  MenuItem,
  Paper,
  Select,
  SelectChangeEvent,
  TextField,
  Typography,
  Alert,
  CircularProgress,
  Grid,
  Chip,
  Snackbar,
} from '@mui/material';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import SaveIcon from '@mui/icons-material/Save';
import SettingsIcon from '@mui/icons-material/Settings';
import CalendarTodayIcon from '@mui/icons-material/CalendarToday';
import InfoIcon from '@mui/icons-material/Info';
import WarningAmberIcon from '@mui/icons-material/WarningAmber';
import ErrorIcon from '@mui/icons-material/Error';
import sprintService, {
  EndDayOfWeek,
  END_DAY_OF_WEEK_LABELS,
  CalculateEndDateResponse,
} from '../../services/sprintService';

// Default values
const DEFAULT_BUSINESS_DAYS = 10;
const DEFAULT_END_DAY_OF_WEEK: EndDayOfWeek = 'FRIDAY';

// Debounce delay for API calls (ms)
const DEBOUNCE_DELAY = 500;

const SprintSettings: React.FC = () => {
  const {productId} = useParams<{productId: string}>();
  const [loading, setLoading] = useState<boolean>(false);
  const [fetching, setFetching] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [successMessage, setSuccessMessage] = useState<string | null>(null);
  const [snackbarOpen, setSnackbarOpen] = useState(false);

  // Form state
  const [businessDays, setBusinessDays] = useState<number>(DEFAULT_BUSINESS_DAYS);
  const [endDayOfWeek, setEndDayOfWeek] = useState<EndDayOfWeek>(DEFAULT_END_DAY_OF_WEEK);
  const [businessDaysError, setBusinessDaysError] = useState<string>('');
  const [endDayOfWeekError, setEndDayOfWeekError] = useState<string>('');

  // Sprint 0 status check
  const [isSprint0Planned, setIsSprint0Planned] = useState<boolean>(false);

  // Backend-calculated end date state
  const [calculatedEndDate, setCalculatedEndDate] = useState<CalculateEndDateResponse | null>(null);
  const [endDateLoading, setEndDateLoading] = useState<boolean>(false);
  const [endDateError, setEndDateError] = useState<string | null>(null);
  const debounceTimerRef = useRef<ReturnType<typeof setTimeout> | null>(null);

  useEffect(() => {
    const fetchData = async () => {
      if (!productId) return;
      try {
        setFetching(true);

        // Fetch sprint settings
        try {
          const settings = await sprintService.getSprintSettings(productId);
          setBusinessDays(settings.businessDays);
          setEndDayOfWeek(settings.endDayOfWeek);
        } catch {
          // If settings don't exist yet, use defaults
          setBusinessDays(DEFAULT_BUSINESS_DAYS);
          setEndDayOfWeek(DEFAULT_END_DAY_OF_WEEK);
        }

        // Check if Sprint 0 is in PLANNED status
        try {
          const sprints = await sprintService.getAllSprints();
          const sprint0 = sprints.find(s => s.productId === productId && s.title === 'Sprint 0');
          setIsSprint0Planned(sprint0?.status === 'PLANNED' || false);
        } catch {
          // If we can't check, allow editing by default
          setIsSprint0Planned(true);
        }
      } catch (err) {
        setError('スプリント設定の読み込みに失敗しました。');
        console.error('Error loading sprint settings:', err);
      } finally {
        setFetching(false);
      }
    };

    fetchData();
  }, [productId]);

  // Fetch calculated end date from backend with debounce
  const fetchCalculatedEndDate = useCallback(async () => {
    if (!productId) return;
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    const startDateStr = today.toISOString().split('T')[0];

    try {
      setEndDateLoading(true);
      setEndDateError(null);
      const result = await sprintService.calculateEndDate(productId, startDateStr);
      setCalculatedEndDate(result);
    } catch (err: any) {
      const message = err.response?.data?.detail || '終了日の計算に失敗しました';
      setEndDateError(message);
      console.error('Error calculating end date:', err);
    } finally {
      setEndDateLoading(false);
    }
  }, [productId]);

  // Debounced effect to fetch end date when settings change
  useEffect(() => {
    if (!isSprint0Planned) return;

    // Clear existing timer
    if (debounceTimerRef.current) {
      clearTimeout(debounceTimerRef.current);
    }

    // Set new timer
    debounceTimerRef.current = setTimeout(() => {
      fetchCalculatedEndDate();
    }, DEBOUNCE_DELAY);

    // Cleanup
    return () => {
      if (debounceTimerRef.current) {
        clearTimeout(debounceTimerRef.current);
      }
    };
  }, [businessDays, endDayOfWeek, fetchCalculatedEndDate, isSprint0Planned, productId]);

  // Get day of week label from backend response
  const getEndDayOfWeekLabel = (label: string): string => {
    return END_DAY_OF_WEEK_LABELS[label as EndDayOfWeek] || label;
  };

  const validateForm = (): boolean => {
    let isValid = true;

    if (businessDays < 1) {
      setBusinessDaysError('営業日数は1以上を入力してください');
      isValid = false;
    } else if (businessDays > 30) {
      setBusinessDaysError('営業日数は30以下を入力してください');
      isValid = false;
    } else {
      setBusinessDaysError('');
    }

    if (!endDayOfWeek) {
      setEndDayOfWeekError('終了曜日を選択してください');
      isValid = false;
    } else {
      setEndDayOfWeekError('');
    }

    return isValid;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!validateForm()) {
      return;
    }

    if (!productId) return;
    setLoading(true);
    setError(null);
    setSuccessMessage(null);

    try {
      await sprintService.updateSprintSettings({
        productId,
        businessDays,
        endDayOfWeek,
      });

      setSuccessMessage('スプリント設定が正常に保存されました');
      setSnackbarOpen(true);
    } catch (err: any) {
      const message = err.response?.data?.detail || 'スプリント設定の保存に失敗しました';
      setError(message);
      console.error('Error updating sprint settings:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleBusinessDaysChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = parseInt(e.target.value, 10);
    if (isNaN(value)) {
      setBusinessDays(0);
    } else {
      setBusinessDays(value);
    }
  };

  const handleEndDayOfWeekChange = (e: SelectChangeEvent<EndDayOfWeek>) => {
    setEndDayOfWeek(e.target.value as EndDayOfWeek);
  };

  const handleSnackbarClose = () => {
    setSnackbarOpen(false);
    setSuccessMessage(null);
  };

  const isEditable = isSprint0Planned;

  if (fetching) {
    return (
      <Container maxWidth="md">
        <Box sx={{my: 4, display: 'flex', justifyContent: 'center', alignItems: 'center'}}>
          <CircularProgress />
          <Typography sx={{ml: 2}}>スプリント設定を読み込み中...</Typography>
        </Box>
      </Container>
    );
  }

  return (
    <Container maxWidth="md">
      <Box sx={{my: 4}}>
        <Box display="flex" alignItems="center" mb={4}>
          <Button
            component={RouterLink}
            to={`/products/${productId}/scrum-teams`}
            startIcon={<ArrowBackIcon />}
            sx={{mr: 2}}
          >
            戻る
          </Button>
          <SettingsIcon sx={{mr: 1, color: 'primary.main'}} />
          <Typography variant="h4" component="h1" gutterBottom>
            スプリント設定
          </Typography>
        </Box>

        {/* Sprint 0 status notice */}
        {!isEditable && (
          <Alert severity="warning" sx={{mb: 3}} icon={<InfoIcon fontSize="inherit" />}>
            <Typography variant="body2">
              スプリント設定はスプリント0（準備中）の間にのみ変更できます。
              現在スプリントが開始されているため、設定は読み取り専用です。
            </Typography>
          </Alert>
        )}

        {error && (
          <Alert severity="error" sx={{mb: 3}}>
            {error}
          </Alert>
        )}

        <Paper sx={{p: 4}}>
          <form onSubmit={handleSubmit}>
            <Typography variant="h6" gutterBottom sx={{mb: 3}}>
              スプリント期間設定
            </Typography>

            <Grid container spacing={3}>
              {/* Business Days Input */}
              <Grid item xs={12} sm={6}>
                <TextField
                  fullWidth
                  label="スプリント期間（営業日）"
                  type="number"
                  value={businessDays}
                  onChange={handleBusinessDaysChange}
                  error={!!businessDaysError}
                  helperText={businessDaysError || 'スプリントの営業日数（1〜30日）'}
                  InputProps={{
                    inputProps: {min: 1, max: 30},
                    disabled: !isEditable,
                  }}
                  disabled={!isEditable}
                  required
                  sx={{mb: 2}}
                />
              </Grid>

              {/* End Day of Week Select */}
              <Grid item xs={12} sm={6}>
                <FormControl fullWidth error={!!endDayOfWeekError} disabled={!isEditable}>
                  <InputLabel>スプリント終了曜日</InputLabel>
                  <Select
                    value={endDayOfWeek}
                    onChange={handleEndDayOfWeekChange}
                    label="スプリント終了曜日"
                    required
                  >
                    <MenuItem value="MONDAY">月曜日</MenuItem>
                    <MenuItem value="TUESDAY">火曜日</MenuItem>
                    <MenuItem value="WEDNESDAY">水曜日</MenuItem>
                    <MenuItem value="THURSDAY">木曜日</MenuItem>
                    <MenuItem value="FRIDAY">金曜日</MenuItem>
                  </Select>
                  <FormHelperText>
                    {endDayOfWeekError || 'スプリントが終了する曜日'}
                  </FormHelperText>
                </FormControl>
              </Grid>

              {/* Preview End Date - Backend Calculated */}
              <Grid item xs={12}>
                <Paper
                  variant="outlined"
                  sx={{
                    p: 3,
                    bgcolor: 'action.hover',
                    border: '1px dashed',
                    borderColor: 'divider',
                  }}
                >
                  <Box display="flex" alignItems="center" mb={2}>
                    <CalendarTodayIcon sx={{mr: 1, color: 'primary.main'}} />
                    <Typography variant="subtitle1" fontWeight="bold">
                      スプリント終了日（自動計算結果）
                    </Typography>
                    {endDateLoading && (
                      <Box display="flex" alignItems="center" ml={2}>
                        <CircularProgress size={20} sx={{mr: 1}} />
                        <Typography variant="body2" color="text.secondary">
                          計算中...
                        </Typography>
                      </Box>
                    )}
                  </Box>

                  <Typography variant="body1" color="text.secondary" sx={{mb: 2}}>
                    今日から{businessDays}営業日後、{END_DAY_OF_WEEK_LABELS[endDayOfWeek]}に終了
                  </Typography>

                  {/* Error state */}
                  {endDateError && (
                    <Alert severity="error" icon={<ErrorIcon fontSize="inherit" />} sx={{mb: 2}}>
                      {endDateError}
                    </Alert>
                  )}

                  {/* Calculated end date display */}
                  {!endDateLoading && !endDateError && calculatedEndDate && (
                    <Box>
                      {/* Past date warning */}
                      {calculatedEndDate.isPastDate && (
                        <Alert
                          severity="warning"
                          icon={<WarningAmberIcon fontSize="inherit" />}
                          sx={{mb: 2}}
                        >
                          <Typography variant="body2">
                            計算された終了日（
                            {new Date(calculatedEndDate.endDate).toLocaleDateString('ja-JP', {
                              year: 'numeric',
                              month: 'long',
                              day: 'numeric',
                            })}
                            ）は過去の日付になっています。営業日数または終了曜日を見直してください。
                          </Typography>
                        </Alert>
                      )}

                      <Box display="flex" alignItems="center" gap={1}>
                        <Chip
                          label={`${new Date(calculatedEndDate.endDate).toLocaleDateString('ja-JP', {
                            year: 'numeric',
                            month: 'long',
                            day: 'numeric',
                          })}（${getEndDayOfWeekLabel(calculatedEndDate.endDayOfWeekLabel).replace('曜日', '')}）`}
                          color={calculatedEndDate.isPastDate ? 'warning' : 'primary'}
                          variant="outlined"
                        />
                      </Box>
                      <Typography variant="caption" color="text.secondary" sx={{mt: 1, display: 'block'}}>
                        ※ 土・日曜日および祝日が除外されています。カレンダーレジストリを利用して計算されています。
                      </Typography>
                    </Box>
                  )}

                  {/* Loading initial state */}
                  {!endDateLoading && !endDateError && !calculatedEndDate && isSprint0Planned && (
                    <Typography variant="body2" color="text.disabled">
                      終了日を計算しています...
                    </Typography>
                  )}

                  {/* Disabled state when not in Sprint 0 */}
                  {!isSprint0Planned && (
                    <Typography variant="body2" color="text.disabled">
                      スプリント終了日のプレビューはスプリント0中にのみ利用できます。
                    </Typography>
                  )}
                </Paper>
              </Grid>
            </Grid>

            {/* Action Buttons */}
            <Box display="flex" justifyContent="flex-end" gap={2} sx={{mt: 4}}>
              <Button
                component={RouterLink}
                to={`/products/${productId}/scrum-teams`}
                variant="outlined"
              >
                キャンセル
              </Button>
              <Button
                type="submit"
                variant="contained"
                color="primary"
                startIcon={<SaveIcon />}
                disabled={loading || !isEditable}
              >
                {loading ? '保存中...' : '保存'}
              </Button>
            </Box>
          </form>
        </Paper>

        {/* Current Settings Info */}
        <Paper sx={{p: 3, mt: 3}}>
          <Typography variant="h6" gutterBottom>
            現在の設定
          </Typography>
          <Grid container spacing={2}>
            <Grid item xs={12} sm={6}>
              <Typography variant="body2" color="text.secondary">
                スプリント期間
              </Typography>
              <Typography variant="body1">
                {businessDays}営業日
              </Typography>
            </Grid>
            <Grid item xs={12} sm={6}>
              <Typography variant="body2" color="text.secondary">
                終了曜日
              </Typography>
              <Typography variant="body1">
                {END_DAY_OF_WEEK_LABELS[endDayOfWeek]}
              </Typography>
            </Grid>
          </Grid>
        </Paper>
      </Box>

      {/* Success Snackbar */}
      <Snackbar
        open={snackbarOpen}
        autoHideDuration={6000}
        onClose={handleSnackbarClose}
        anchorOrigin={{vertical: 'bottom', horizontal: 'center'}}
      >
        <Alert onClose={handleSnackbarClose} severity="success" sx={{width: '100%'}}>
          {successMessage}
        </Alert>
      </Snackbar>
    </Container>
  );
};

export default SprintSettings;
