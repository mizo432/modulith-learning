import React, {useCallback, useEffect, useState} from 'react';
import {Link as RouterLink, useParams, useNavigate} from 'react-router-dom';
import {
  Box,
  Button,
  Container,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Typography,
  IconButton,
  Tooltip,
  Chip,
  TextField,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  TableSortLabel,
  TablePagination,
  TableRow as MuiTableRow
} from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import ScheduleIcon from '@mui/icons-material/Schedule';
import PlayArrowIcon from '@mui/icons-material/PlayArrow';
import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import CancelIcon from '@mui/icons-material/Cancel';
import sprintService, {Sprint, isSprint0} from '../../services/sprintService';
import SprintCapacityCell from '../../components/SprintCapacityCell';

// Status chip color mapping
const getStatusChip = (status: Sprint['status']) => {
  switch (status) {
    case 'PLANNED':
      return <Chip label="準備中" color="default" size="small" icon={<ScheduleIcon/>}/>;
    case 'ACTIVE':
      return <Chip label="進行中" color="primary" size="small" icon={<PlayArrowIcon/>}/>;
    case 'COMPLETED':
      return <Chip label="完了" color="success" size="small" icon={<CheckCircleIcon/>}/>;
    case 'CANCELLED':
      return <Chip label="キャンセル" color="error" size="small" icon={<CancelIcon/>}/>;
    default:
      return <Chip label={status} size="small"/>;
  }
};

type SortOrder = 'asc' | 'desc';

const SprintList: React.FC = () => {
  const {productId, scrumTeamId} = useParams<{productId: string; scrumTeamId: string}>();
  const navigate = useNavigate();
  const [sprints, setSprints] = useState<Sprint[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  // Sort state
  const [orderBy, setOrderBy] = useState<'sprintNumber' | 'startDate' | 'status'>('sprintNumber');
  const [order, setOrder] = useState<SortOrder>('asc');

  // Pagination state
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);

  // Edit date dialog state
  const [editDialogOpen, setEditDialogOpen] = useState(false);
  const [editingSprint, setEditingSprint] = useState<Sprint | null>(null);
  const [editStartDate, setEditStartDate] = useState('');
  const [editEndDate, setEditEndDate] = useState('');

  const fetchSprints = useCallback(async () => {
    if (!scrumTeamId) return;
    try {
      setLoading(true);
      const data = await sprintService.getSprintsByTeamId(scrumTeamId);
      setSprints(data);
      setError(null);
    } catch (err) {
      setError('スプリント一覧の取得に失敗しました。');
      console.error('Error fetching sprints:', err);
    } finally {
      setLoading(false);
    }
  }, [scrumTeamId]);

  useEffect(() => {
    fetchSprints();
  }, [scrumTeamId, fetchSprints]);

  // Handle sort
  const handleRequestSort = (property: 'sprintNumber' | 'startDate' | 'status') => {
    const isAsc = orderBy === property && order === 'asc';
    setOrder(isAsc ? 'desc' : 'asc');
    setOrderBy(property);
  };

  // Handle pagination
  const handleChangePage = (_event: unknown, newPage: number) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event: React.ChangeEvent<HTMLInputElement>) => {
    setRowsPerPage(parseInt(event.target.value, 10));
    setPage(0);
  };

  // Sort sprints
  const sortedSprints = [...sprints].sort((a, b) => {
    // Sprint 0 always comes first
    if (isSprint0(a) && !isSprint0(b)) return -1;
    if (!isSprint0(a) && isSprint0(b)) return 1;

    switch (orderBy) {
      case 'sprintNumber':
        return order === 'asc' ? a.sprintNumber - b.sprintNumber : b.sprintNumber - a.sprintNumber;
      case 'startDate':
        return order === 'asc'
          ? new Date(a.startDate).getTime() - new Date(b.startDate).getTime()
          : new Date(b.startDate).getTime() - new Date(a.startDate).getTime();
      case 'status':
        return order === 'asc' ? a.status.localeCompare(b.status) : b.status.localeCompare(a.status);
      default:
        return 0;
    }
  });

  // Paginated sprints
  const paginatedSprints = sortedSprints.slice(
    page * rowsPerPage,
    page * rowsPerPage + rowsPerPage
  );

  // Handle delete sprint (not allowed for Sprint 0)
  const handleDeleteSprint = async (sprint: Sprint) => {
    if (isSprint0(sprint)) {
      alert('スプリント0は削除できません。');
      return;
    }
    if (!window.confirm('このスプリントを削除してもよろしいですか？')) return;
    try {
      // Note: Delete API would be added to backend in a future update
      alert('スプリントの削除機能は現在準備中です。');
    } catch (err) {
      setError('スプリントの削除に失敗しました。');
      console.error('Error deleting sprint:', err);
    }
  };

  // Handle edit dates dialog open
  const handleOpenEditDialog = (sprint: Sprint) => {
    setEditingSprint(sprint);
    setEditStartDate(sprint.startDate.split('T')[0]);
    setEditEndDate(sprint.endDate.split('T')[0]);
    setEditDialogOpen(true);
  };

  // Handle edit dates dialog close
  const handleCloseEditDialog = () => {
    setEditDialogOpen(false);
    setEditingSprint(null);
  };

  // Handle save dates
  const handleSaveDates = async () => {
    if (!editingSprint) return;
    try {
      // Update the sprint dates via API
      const updatedSprint = {
        ...editingSprint,
        startDate: editStartDate,
        endDate: editEndDate
      };
      // Note: The update API for dates would be added to backend
      // For now, update local state
      setSprints(prev => prev.map(s =>
        s.id === editingSprint.id ? updatedSprint : s
      ));
      handleCloseEditDialog();
    } catch (err) {
      setError('日付の更新に失敗しました。');
      console.error('Error updating sprint dates:', err);
    }
  };

  // Handle create new sprint
  const handleCreateSprint = () => {
    // Navigate to sprint create page
    if (productId && scrumTeamId) {
      navigate(`/products/${productId}/scrum-teams/edit/${scrumTeamId}/sprints/create`);
    }
  };

  return (
      <Container maxWidth="lg">
        <Box sx={{my: 4}}>
          <Box display="flex" alignItems="center" mb={4}>
            <Button
                component={RouterLink}
                to={`/products/${productId}/scrum-teams/edit/${scrumTeamId}`}
                startIcon={<ArrowBackIcon/>}
                sx={{mr: 2}}
            >
              戻る
            </Button>
            <Typography variant="h4" component="h1" gutterBottom>
              スプリント一覧
            </Typography>
          </Box>

          {error && (
              <Paper sx={{p: 2, mb: 2, bgcolor: 'error.light', color: 'error.contrastText'}}>
                <Typography>{error}</Typography>
              </Paper>
          )}

          <Box display="flex" justifyContent="space-between" alignItems="center" mb={2}>
            <Typography variant="h6">スプリント</Typography>
            <Button
                onClick={handleCreateSprint}
                variant="contained"
                color="primary"
                startIcon={<AddIcon/>}
            >
              スプリント作成
            </Button>
          </Box>

          {loading ? (
              <Typography>スプリントを読み込み中...</Typography>
          ) : sortedSprints.length === 0 ? (
              <Paper sx={{p: 4, textAlign: 'center'}}>
                <Typography variant="h6">スプリントが見つかりません</Typography>
                <Typography variant="body1" sx={{mt: 2}}>
                  スプリント0が自動的に作成されます。
                </Typography>
              </Paper>
          ) : (
              <>
                <TableContainer component={Paper}>
                  <Table>
                    <TableHead>
                      <TableRow>
                        <TableCell>
                          <TableSortLabel
                              active={orderBy === 'sprintNumber'}
                              direction={orderBy === 'sprintNumber' ? order : 'asc'}
                              onClick={() => handleRequestSort('sprintNumber')}
                          >
                            番号
                          </TableSortLabel>
                        </TableCell>
                        <TableCell>タイトル</TableCell>
                        <TableCell>チーム</TableCell>
                        <TableCell>
                          <TableSortLabel
                              active={orderBy === 'startDate'}
                              direction={orderBy === 'startDate' ? order : 'asc'}
                              onClick={() => handleRequestSort('startDate')}
                          >
                            開始日
                          </TableSortLabel>
                        </TableCell>
                        <TableCell>終了日</TableCell>
                        <TableCell>
                          <TableSortLabel
                              active={orderBy === 'status'}
                              direction={orderBy === 'status' ? order : 'asc'}
                              onClick={() => handleRequestSort('status')}
                          >
                            ステータス
                          </TableSortLabel>
                        </TableCell>
                        <TableCell>容量</TableCell>
                        <TableCell align="right">操作</TableCell>
                      </TableRow>
                    </TableHead>
                    <TableBody>
                      {paginatedSprints.map((sprint) => {
                        const sprintZero = isSprint0(sprint);
                        return (
                            <MuiTableRow
                                key={sprint.id}
                                sx={{
                                  bgcolor: sprintZero ? 'action.hover' : 'inherit',
                                  '&:hover': {bgcolor: 'action.hover'},
                                }}
                            >
                              <TableCell>
                                {sprintZero ? (
                                    <Box sx={{display: 'flex', alignItems: 'center', gap: 1}}>
                                      <ScheduleIcon color="primary"/>
                                      <Typography variant="body2" fontWeight="bold">
                                        Sprint 0
                                      </Typography>
                                    </Box>
                                ) : (
                                    `Sprint ${sprint.sprintNumber}`
                                )}
                              </TableCell>
                              <TableCell>
                                <Box>
                                  <Typography variant="body2" fontWeight={sprintZero ? 'bold' : 'normal'}>
                                    {sprint.title}
                                  </Typography>
                                  {sprint.goal && (
                                      <Typography variant="caption" color="text.secondary">
                                        {sprint.goal}
                                      </Typography>
                                  )}
                                </Box>
                              </TableCell>
                              <TableCell>{sprint.teamName || '未設定'}</TableCell>
                              <TableCell>
                                {sprint.startDate ? (
                                    sprint.startDate === 'undefined' || sprint.startDate === 'null'
                                        ? '未設定'
                                        : new Date(sprint.startDate).toLocaleDateString('ja-JP')
                                ) : '未設定'}
                              </TableCell>
                              <TableCell>
                                {sprint.endDate ? (
                                    sprint.endDate === 'undefined' || sprint.endDate === 'null'
                                        ? '未設定'
                                        : new Date(sprint.endDate).toLocaleDateString('ja-JP')
                                ) : '未設定'}
                              </TableCell>
                              <TableCell>{getStatusChip(sprint.status)}</TableCell>
                              <TableCell>
                                {sprintZero ? '-' : <SprintCapacityCell sprintId={sprint.id}/>}
                              </TableCell>
                              <TableCell align="right">
                                <Box sx={{display: 'flex', justifyContent: 'flex-end'}}>
                                  {/* Date edit button - enabled for Sprint 0 */}
                                  <Tooltip title="日付編集">
                                    <IconButton
                                        onClick={() => handleOpenEditDialog(sprint)}
                                        size="small"
                                        sx={{mr: 1}}
                                    >
                                      <EditIcon/>
                                    </IconButton>
                                  </Tooltip>

                                  {/* Delete button - disabled for Sprint 0 */}
                                  {!sprintZero && (
                                      <Tooltip title="削除">
                                        <IconButton
                                            onClick={() => handleDeleteSprint(sprint)}
                                            size="small"
                                            color="error"
                                        >
                                          <DeleteIcon/>
                                        </IconButton>
                                      </Tooltip>
                                  )}
                                  {sprintZero && (
                                      <Tooltip title="スプリント0は削除できません">
                                        <IconButton disabled size="small" color="error">
                                          <DeleteIcon/>
                                        </IconButton>
                                      </Tooltip>
                                  )}
                                </Box>
                              </TableCell>
                            </MuiTableRow>
                        );
                      })}
                    </TableBody>
                  </Table>
                </TableContainer>
                <TablePagination
                    component="div"
                    count={sortedSprints.length}
                    page={page}
                    onPageChange={handleChangePage}
                    rowsPerPage={rowsPerPage}
                    onRowsPerPageChange={handleChangeRowsPerPage}
                    labelRowsPerPage="表示数"
                    labelDisplayedRows={({from, to, count}) => `${from}-${to} / ${count}`}
                />
              </>
          )}
        </Box>

        {/* Edit Dates Dialog */}
        <Dialog open={editDialogOpen} onClose={handleCloseEditDialog}>
          <DialogTitle>スプリント日付編集</DialogTitle>
          <DialogContent>
            {editingSprint && (
                <Box sx={{mt: 1}}>
                  <Typography variant="body2" gutterBottom>
                    {isSprint0(editingSprint) ? 'Sprint 0' : `Sprint ${editingSprint.sprintNumber}`} - {editingSprint.title}
                  </Typography>
                  <DialogContentText sx={{mb: 2}}>
                    開始日と終了日を設定してください。
                  </DialogContentText>
                  <TextField
                      label="開始日"
                      type="date"
                      fullWidth
                      value={editStartDate}
                      onChange={(e) => setEditStartDate(e.target.value)}
                      InputLabelProps={{shrink: true}}
                      sx={{mb: 2}}
                  />
                  <TextField
                      label="終了日"
                      type="date"
                      fullWidth
                      value={editEndDate}
                      onChange={(e) => setEditEndDate(e.target.value)}
                      InputLabelProps={{shrink: true}}
                  />
                </Box>
            )}
          </DialogContent>
          <DialogActions>
            <Button onClick={handleCloseEditDialog}>キャンセル</Button>
            <Button onClick={handleSaveDates} variant="contained" color="primary">
              保存
            </Button>
          </DialogActions>
        </Dialog>
      </Container>
  );
};

export default SprintList;
