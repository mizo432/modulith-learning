import React, {useCallback, useEffect, useState} from 'react';
import {
  Alert,
  Box,
  Button,
  Chip,
  Container,
  FormControl,
  InputLabel,
  MenuItem,
  Select,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  TextField,
  Typography,
} from '@mui/material';
import {useAuth} from '../contexts/AuthContext';
import {
  getJobDefinitions,
  getJobExecutions,
  JobDefinition,
  JobExecution,
  startJob,
} from '../services/jobService';

const STATE_LABELS: Record<string, string> = {
  PENDING: '待機中',
  RUNNING: '実行中',
  SUCCESS: '成功',
  FAILED: '失敗',
  UNKNOWN: '結果不明',
  RE_EXECUTED: '再実行',
  RESUMED: '再開',
  CANCELLED: 'キャンセル済み',
};

const JobLaunchPage: React.FC = () => {
  const {employeeId} = useAuth();
  const [definitions, setDefinitions] = useState<JobDefinition[]>([]);
  const [selectedJobCode, setSelectedJobCode] = useState('');
  const [parameters, setParameters] = useState('');
  const [executions, setExecutions] = useState<JobExecution[]>([]);
  const [successMessage, setSuccessMessage] = useState('');
  const [errorMessage, setErrorMessage] = useState('');

  const loadExecutions = useCallback(async () => {
    try {
      setExecutions(await getJobExecutions());
    } catch {
      // 実行一覧の取得失敗は起動操作を妨げない
    }
  }, []);

  useEffect(() => {
    (async () => {
      try {
        setDefinitions(await getJobDefinitions());
      } catch {
        setErrorMessage('ジョブ定義の取得に失敗しました');
      }
    })();
    loadExecutions();
  }, [loadExecutions]);

  const selectedDefinition = definitions.find(d => d.jobCode === selectedJobCode);

  const handleLaunch = async () => {
    setSuccessMessage('');
    setErrorMessage('');
    try {
      await startJob({
        jobCode: selectedJobCode,
        actorEmployeeId: employeeId ?? '',
        parameters,
      });
      setSuccessMessage('ジョブを起動しました');
      await loadExecutions();
    } catch (err: any) {
      setErrorMessage(err?.response?.data?.detail ?? 'ジョブの起動に失敗しました');
    }
  };

  return (
      <Container>
        <Typography variant="h4" gutterBottom>ジョブ手動起動</Typography>
        {successMessage && <Alert severity="success" sx={{mb: 2}}>{successMessage}</Alert>}
        {errorMessage && <Alert severity="error" sx={{mb: 2}}>{errorMessage}</Alert>}
        <FormControl fullWidth sx={{mb: 2}}>
          <InputLabel id="job-select-label">ジョブ</InputLabel>
          <Select
              labelId="job-select-label"
              label="ジョブ"
              value={selectedJobCode}
              onChange={(e) => setSelectedJobCode(e.target.value)}
          >
            {definitions.map(d => (
                <MenuItem key={d.id} value={d.jobCode} disabled={!d.isActive}>
                  {d.jobCode}
                </MenuItem>
            ))}
          </Select>
        </FormControl>
        {selectedDefinition?.parameterSpec && (
            <Typography variant="body2" sx={{mb: 2}}>
              {selectedDefinition.parameterSpec}
            </Typography>
        )}
        <TextField
            label="実行パラメータ"
            multiline
            rows={4}
            fullWidth
            value={parameters}
            onChange={(e) => setParameters(e.target.value)}
            helperText="1行に1件、key=value形式で入力してください"
            sx={{mb: 2}}
        />
        <Box sx={{mb: 4}}>
          <Button variant="contained" onClick={handleLaunch} disabled={!selectedJobCode}>
            起動する
          </Button>
        </Box>
        <Typography variant="h6" gutterBottom>実行状況</Typography>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>実行ID</TableCell>
              <TableCell>ジョブ</TableCell>
              <TableCell>状態</TableCell>
              <TableCell>開始日時</TableCell>
              <TableCell>終了日時</TableCell>
              <TableCell>エラー</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {executions.map(ex => (
                <TableRow key={ex.id}>
                  <TableCell>{ex.executionId}</TableCell>
                  <TableCell>{ex.jobCode}</TableCell>
                  <TableCell>
                    <Chip label={STATE_LABELS[ex.state] ?? ex.state} size="small"/>
                  </TableCell>
                  <TableCell>{ex.startedAt ?? ''}</TableCell>
                  <TableCell>{ex.completedAt ?? ''}</TableCell>
                  <TableCell>{ex.errorMessage ?? ''}</TableCell>
                </TableRow>
            ))}
          </TableBody>
        </Table>
      </Container>
  );
};

export default JobLaunchPage;
