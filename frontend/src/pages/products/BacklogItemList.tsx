import React, {useCallback, useEffect, useState} from 'react';
import {useNavigate, useParams} from 'react-router-dom';
import {
  Alert,
  Box,
  Button,
  Chip,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  FormControl,
  InputLabel,
  MenuItem,
  Paper,
  Select,
  SelectChangeEvent,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  TextField,
  Tooltip,
  Typography,
} from '@mui/material';
import backlogItemService, {
  BacklogItem,
  BacklogItemStatus,
} from '../../services/backlogItemService';
import scrumTeamService, {ScrumTeam} from '../../services/scrumTeamService';
import {valueToColor, valueToBackgroundColor} from '../../utils/valueColor';

const STATUS_LABELS: Record<BacklogItemStatus, string> = {
  SUGGESTED: '提案',
  ACCEPTED: '承認済み',
  REJECTED: '却下',
};

const STATUS_COLORS: Record<BacklogItemStatus, 'warning' | 'success' | 'error'> = {
  SUGGESTED: 'warning',
  ACCEPTED: 'success',
  REJECTED: 'error',
};

const BacklogItemList: React.FC = () => {
  const {productId, scrumTeamId} = useParams<{ productId: string; scrumTeamId: string }>();
  const navigate = useNavigate();

  const [items, setItems] = useState<BacklogItem[]>([]);
  const [team, setTeam] = useState<ScrumTeam | null>(null);
  const [statusFilter, setStatusFilter] = useState<'' | BacklogItemStatus>('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [successMessage, setSuccessMessage] = useState<string | null>(null);

  // 新規追加フォーム
  const [formOpen, setFormOpen] = useState(false);
  const [formTitle, setFormTitle] = useState('');
  const [formDescription, setFormDescription] = useState('');
  const [formPriority, setFormPriority] = useState('');
  const [formCreatorName, setFormCreatorName] = useState(
    localStorage.getItem('user') ?? '',
  );

  // 状態変更の確認ダイアログ
  const [confirmTarget, setConfirmTarget] = useState<{
    item: BacklogItem;
    status: BacklogItemStatus;
  } | null>(null);

  // 現在の従業員ID（ロール情報の正式な取得方法は Issue #573 で検討中）
  const currentEmployeeId = localStorage.getItem('employeeId');
  const canChangeStatus =
    !!currentEmployeeId &&
    !!team &&
    (team.poEmployeeId === currentEmployeeId || team.smEmployeeId === currentEmployeeId);

  const loadItems = useCallback(async () => {
    if (!scrumTeamId) return;
    setLoading(true);
    try {
      const data = await backlogItemService.getBacklogItemsByTeam(
        scrumTeamId,
        statusFilter || undefined,
      );
      setItems(data);
    } catch (err) {
      console.error('Failed to load backlog items:', err);
      setError('バックログアイテムの読み込みに失敗しました。');
    } finally {
      setLoading(false);
    }
  }, [scrumTeamId, statusFilter]);

  useEffect(() => {
    loadItems();
  }, [loadItems]);

  useEffect(() => {
    const loadTeam = async () => {
      if (!productId || !scrumTeamId) return;
      try {
        const data = await scrumTeamService.getScrumTeamById(productId, scrumTeamId);
        setTeam(data);
      } catch (err) {
        console.error('Failed to load scrum team:', err);
      }
    };
    loadTeam();
  }, [productId, scrumTeamId]);

  const handleFilterChange = (event: SelectChangeEvent) => {
    setStatusFilter(event.target.value as '' | BacklogItemStatus);
  };

  const handleCreate = async () => {
    if (!scrumTeamId) return;
    setError(null);
    try {
      await backlogItemService.createBacklogItem(scrumTeamId, {
        title: formTitle,
        description: formDescription || undefined,
        priority: formPriority ? Number(formPriority) : undefined,
        creatorEmployeeId: currentEmployeeId ?? undefined,
        creatorName: formCreatorName,
      });
      setFormOpen(false);
      setFormTitle('');
      setFormDescription('');
      setFormPriority('');
      setSuccessMessage('バックログアイテムを追加しました（初期状態：提案）。');
      await loadItems();
    } catch (err: any) {
      console.error('Failed to create backlog item:', err);
      setError(
        err?.response?.data?.message ?? 'バックログアイテムの追加に失敗しました。',
      );
    }
  };

  const handleConfirmStatusChange = async () => {
    if (!confirmTarget || !currentEmployeeId) return;
    setError(null);
    try {
      await backlogItemService.changeStatus(
        confirmTarget.item.id,
        confirmTarget.status,
        currentEmployeeId,
      );
      setSuccessMessage(
        `「${confirmTarget.item.title}」を${STATUS_LABELS[confirmTarget.status]}に変更しました。`,
      );
      setConfirmTarget(null);
      await loadItems();
    } catch (err: any) {
      console.error('Failed to change status:', err);
      if (err?.response?.status === 403) {
        setError('状態を変更する権限がありません（PO / SM のみ変更できます）。');
      } else {
        setError(err?.response?.data?.message ?? '状態の変更に失敗しました。');
      }
      setConfirmTarget(null);
    }
  };

  if (loading && items.length === 0) {
    return <Typography>読み込み中...</Typography>;
  }

  return (
    <Box>
      <Box sx={{display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2}}>
        <Typography variant="h5">
          バックログアイテム{team ? `（${team.name}）` : ''}
        </Typography>
        <Button variant="contained" onClick={() => setFormOpen(true)}>
          アイテムを追加
        </Button>
      </Box>

      {error && (
        <Alert severity="error" sx={{mb: 2}} onClose={() => setError(null)}>
          {error}
        </Alert>
      )}
      {successMessage && (
        <Alert severity="success" sx={{mb: 2}} onClose={() => setSuccessMessage(null)}>
          {successMessage}
        </Alert>
      )}

      <FormControl sx={{minWidth: 200, mb: 2}} size="small">
        <InputLabel id="status-filter-label">状態フィルタ</InputLabel>
        <Select
          labelId="status-filter-label"
          label="状態フィルタ"
          value={statusFilter}
          onChange={handleFilterChange}
        >
          <MenuItem value="">すべて</MenuItem>
          <MenuItem value="SUGGESTED">提案</MenuItem>
          <MenuItem value="ACCEPTED">承認済み</MenuItem>
          <MenuItem value="REJECTED">却下</MenuItem>
        </Select>
      </FormControl>

      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>タイトル</TableCell>
              <TableCell>状態</TableCell>
              <TableCell>Value</TableCell>
              <TableCell>優先度</TableCell>
              <TableCell>作成者</TableCell>
              <TableCell>作成日時</TableCell>
              <TableCell align="right">操作</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {items.map((item) => {
              const acceptDisabled = item.value == null;
              return (
                <TableRow key={item.id} hover>
                  <TableCell>{item.title}</TableCell>
                  <TableCell>
                    <Chip
                      label={item.statusLabel || STATUS_LABELS[item.status]}
                      color={STATUS_COLORS[item.status]}
                      size="small"
                    />
                  </TableCell>
                  <TableCell
                    data-testid={`value-cell-${item.id}`}
                  >
                    <Box
                      component="span"
                      sx={{
                        fontWeight: 'bold',
                        padding: '2px 4px',
                        borderRadius: 1,
                      }}
                      style={{
                        backgroundColor: item.value != null ? valueToBackgroundColor(item.value) : undefined,
                        color: item.value != null ? valueToColor(item.value) : undefined,
                      }}
                    >
                      {item.value ?? '-'}
                    </Box>
                  </TableCell>
                  <TableCell>{item.priority ?? '-'}</TableCell>
                  <TableCell>{item.creatorName}</TableCell>
                  <TableCell>{new Date(item.createdAt).toLocaleString()}</TableCell>
                  <TableCell align="right">
                    <Button
                      size="small"
                      onClick={() =>
                        navigate(
                          `/products/${productId}/scrum-teams/${scrumTeamId}/backlog-items/${item.id}`,
                        )
                      }
                    >
                      詳細
                    </Button>
                    {canChangeStatus && item.status === 'SUGGESTED' && (
                      <>
                        <Tooltip
                          title={acceptDisabled ? 'Valueを設定してから承認してください' : ''}
                          arrow
                        >
                          <span>
                            <Button
                              size="small"
                              color="success"
                              disabled={acceptDisabled}
                              onClick={() => setConfirmTarget({item, status: 'ACCEPTED'})}
                            >
                              承認
                            </Button>
                          </span>
                        </Tooltip>
                        <Button
                          size="small"
                          color="error"
                          onClick={() => setConfirmTarget({item, status: 'REJECTED'})}
                        >
                          却下
                        </Button>
                      </>
                    )}
                  </TableCell>
                </TableRow>
              );
            })}
            {items.length === 0 && !loading && (
              <TableRow>
                <TableCell colSpan={7} align="center">
                  バックログアイテムがありません。
                </TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </TableContainer>

      {/* 追加フォーム */}
      <Dialog open={formOpen} onClose={() => setFormOpen(false)} fullWidth maxWidth="sm">
        <DialogTitle>バックログアイテムを追加</DialogTitle>
        <DialogContent>
          <DialogContentText sx={{mb: 1}}>
            追加したアイテムの初期状態は「提案」になります。承認・却下は PO / SM のみが行えます。
          </DialogContentText>
          <DialogContentText sx={{mb: 2, color: 'warning.main'}}>
            Valueは作成後、詳細ページで設定してください（範囲: 0〜100）。
          </DialogContentText>
          <TextField
            label="タイトル"
            fullWidth
            required
            margin="normal"
            value={formTitle}
            onChange={(e) => setFormTitle(e.target.value)}
          />
          <TextField
            label="説明"
            fullWidth
            multiline
            rows={3}
            margin="normal"
            value={formDescription}
            onChange={(e) => setFormDescription(e.target.value)}
          />
          <TextField
            label="優先度"
            fullWidth
            type="number"
            margin="normal"
            value={formPriority}
            onChange={(e) => setFormPriority(e.target.value)}
          />
          <TextField
            label="作成者名"
            fullWidth
            required
            margin="normal"
            value={formCreatorName}
            onChange={(e) => setFormCreatorName(e.target.value)}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setFormOpen(false)}>キャンセル</Button>
          <Button
            variant="contained"
            onClick={handleCreate}
            disabled={!formTitle || !formCreatorName}
          >
            追加
          </Button>
        </DialogActions>
      </Dialog>

      {/* 状態変更の確認ダイアログ */}
      <Dialog open={!!confirmTarget} onClose={() => setConfirmTarget(null)}>
        <DialogTitle>状態変更の確認</DialogTitle>
        <DialogContent>
          <DialogContentText>
            「{confirmTarget?.item.title}」を
            {confirmTarget ? STATUS_LABELS[confirmTarget.status] : ''}
            に変更します。よろしいですか？
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setConfirmTarget(null)}>キャンセル</Button>
          <Button variant="contained" onClick={handleConfirmStatusChange}>
            変更する
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default BacklogItemList;
