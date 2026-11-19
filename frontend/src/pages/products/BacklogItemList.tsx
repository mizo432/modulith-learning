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
import {valueToColor, valueToBackgroundColor, qualityCategoryToLabel, qualityCategoryToTooltip, QualityCategory} from '../../utils/valueColor';
import {useIsVisible} from '../../hooks/useIsVisible';
import {useAutoRefresh} from '../../hooks/useAutoRefresh';
import {useAuth} from '../../contexts/AuthContext';

const REFRESH_INTERVAL = 30000; // 30秒

const STATUS_LABELS: Record<BacklogItemStatus, string> = {
  SUGGESTED: '提案',
  ACCEPTED: '承認済み',
  FROZEN: '凍結',
  READY: '準備完了',
  ESTIMATED: '見積もり済み',
};

const STATUS_COLORS: Record<BacklogItemStatus, 'warning' | 'success' | 'info' | 'primary' | 'default'> = {
  SUGGESTED: 'warning',
  ACCEPTED: 'success',
  FROZEN: 'info',
  READY: 'primary',
  ESTIMATED: 'default',
};

const QUALITY_CATEGORY_COLOR_MAP: Record<QualityCategory, 'success' | 'info' | 'warning' | 'default'> = {
  ATTRACTIVE: 'success',
  ONE_DIMENSIONAL: 'info',
  MUST_BE: 'warning',
  INDIFFERENT: 'default',
};


const BacklogItemList: React.FC = () => {
  const {productId, scrumTeamId} = useParams<{ productId: string; scrumTeamId: string }>();
  const navigate = useNavigate();
  const auth = useAuth();

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
    auth.username ?? '',
  );

  // 状態変更の確認ダイアログ
  const [confirmTarget, setConfirmTarget] = useState<{
    item: BacklogItem;
    status: BacklogItemStatus;
  } | null>(null);

  // 現在の従業員ID（AuthContextから取得）
  const currentEmployeeId = auth.employeeId;
  const canChangeStatus =
    !!currentEmployeeId &&
    !!team &&
    (team.poEmployeeId === currentEmployeeId || team.smEmployeeId === currentEmployeeId);
  const isPo =
    !!currentEmployeeId && !!team && team.poEmployeeId === currentEmployeeId;

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

  // 自動リフレッシュ
  const {isVisible} = useIsVisible();
  const shouldSkipRefresh = formOpen || !!confirmTarget;
  useAutoRefresh(loadItems, REFRESH_INTERVAL, shouldSkipRefresh, isVisible);

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
          <MenuItem value="FROZEN">凍結</MenuItem>
        </Select>
      </FormControl>

      {/* StoryPoint合計表示（Issue #516） */}
      {items.length > 0 && (() => {
        const total = items.reduce((sum, item) => sum + (item.storyPoint ?? 0), 0);
        return (
          <Box sx={{mb: 1, display: 'flex', alignItems: 'center', gap: 1}}>
            <Typography variant="body2" color="text.secondary">
              StoryPoint合計
            </Typography>
            <Box
              component="span"
              sx={{
                fontWeight: 'bold',
                px: 1,
                py: 0.5,
                borderRadius: 1,
                border: '1px solid',
                borderColor: 'primary.light',
                color: 'primary.main',
              }}
            >
              {total}
            </Box>
          </Box>
        );
      })()}

      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>タイトル</TableCell>
              <TableCell>状態</TableCell>
              <TableCell>Value</TableCell>
              <TableCell>StoryPoint</TableCell>
              <TableCell>品質カテゴリ</TableCell>
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
                  <TableCell>
                    {item.storyPoint != null ? item.storyPoint : '-'}
                  </TableCell>
                  <TableCell>
                    {item.qualityCategory ? (
                      <Tooltip
                        title={qualityCategoryToTooltip(item.qualityCategory)}
                        arrow
                        enterDelay={300}
                      >
                        <Chip
                          label={item.qualityCategoryLabel || qualityCategoryToLabel(item.qualityCategory)}
                          color={QUALITY_CATEGORY_COLOR_MAP[item.qualityCategory]}
                          size="small"
                          tabIndex={0}
                        />
                      </Tooltip>
                    ) : (
                      '―'
                    )}
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
                          color="info"
                          onClick={() => setConfirmTarget({item, status: 'FROZEN'})}
                        >
                          凍結
                        </Button>
                      </>
                    )}
                    {isPo && item.status === 'FROZEN' && (
                      <Button
                        size="small"
                        color="warning"
                        onClick={() => setConfirmTarget({item, status: 'SUGGESTED'})}
                      >
                        SUGGESTEDへ戻す
                      </Button>
                    )}
                  </TableCell>
                </TableRow>
              );
            })}
            {items.length === 0 && !loading && (
              <TableRow>
                <TableCell colSpan={9} align="center">
                  バックログアイテムがありません
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
