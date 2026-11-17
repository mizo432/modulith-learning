import React, {useCallback, useEffect, useState} from 'react';
import {useNavigate, useParams} from 'react-router-dom';
import {
  Alert,
  Box,
  Button,
  Chip,
  Divider,
  List,
  ListItem,
  ListItemText,
  Paper,
  Slider,
  TextField,
  Typography,
} from '@mui/material';
import backlogItemService, {
  BacklogItem,
  BacklogItemComment,
  BacklogItemStatus,
} from '../../services/backlogItemService';
import {valueToColor, VALUE_MIN, VALUE_MAX} from '../../utils/valueColor';

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

const BacklogItemDetail: React.FC = () => {
  const {productId, scrumTeamId, backlogItemId} = useParams<{
    productId: string;
    scrumTeamId: string;
    backlogItemId: string;
  }>();
  const navigate = useNavigate();

  const [item, setItem] = useState<BacklogItem | null>(null);
  const [comments, setComments] = useState<BacklogItemComment[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [successMessage, setSuccessMessage] = useState<string | null>(null);
  const [commentBody, setCommentBody] = useState('');
  const [commentAuthorName, setCommentAuthorName] = useState(
    localStorage.getItem('user') ?? '',
  );

  // Value入力（数値型でSliderとTextFieldを双方向連動）
  const [valueInput, setValueInput] = useState<number | null>(null);
  const [valueError, setValueError] = useState(false);
  const [valueHelperText, setValueHelperText] = useState('');
  const [textFieldRaw, setTextFieldRaw] = useState('');

  const load = useCallback(async () => {
    if (!backlogItemId) return;
    try {
      const [itemData, commentData] = await Promise.all([
        backlogItemService.getBacklogItem(backlogItemId),
        backlogItemService.getComments(backlogItemId),
      ]);
      setItem(itemData);
      setComments(commentData);
      setValueInput(itemData.value != null ? itemData.value : null);
      setTextFieldRaw(itemData.value != null ? String(itemData.value) : '');
    } catch (err) {
      console.error('Failed to load backlog item:', err);
      setError('バックログアイテムの読み込みに失敗しました。');
    }
  }, [backlogItemId]);

  useEffect(() => {
    load();
  }, [load]);

  const handleAddComment = async () => {
    if (!backlogItemId) return;
    setError(null);
    try {
      const created = await backlogItemService.addComment(backlogItemId, {
        authorEmployeeId: localStorage.getItem('employeeId') ?? undefined,
        authorName: commentAuthorName,
        body: commentBody,
      });
      setComments((prev) => [...prev, created]);
      setCommentBody('');
    } catch (err: any) {
      console.error('Failed to add comment:', err);
      setError(err?.response?.data?.message ?? 'コメントの追加に失敗しました。');
    }
  };

  const handleSliderChange = (_event: Event, value: number | number[]) => {
    const num = Array.isArray(value) ? value[0] : value;
    setValueInput(num);
    setTextFieldRaw(String(num));
    setValueError(false);
    setValueHelperText('');
  };

  const handleTextFieldChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const raw = e.target.value;
    setTextFieldRaw(raw);
    if (raw === '') {
      setValueInput(null);
      setValueError(false);
      setValueHelperText('');
      return;
    }
    const num = Number(raw);
    if (Number.isNaN(num)) {
      setValueInput(null);
      setValueError(false);
      setValueHelperText('');
      return;
    }
    setValueInput(num);
    if (num < VALUE_MIN || num > VALUE_MAX) {
      setValueError(true);
      setValueHelperText(`${VALUE_MIN}〜${VALUE_MAX}の範囲で入力してください`);
    } else {
      setValueError(false);
      setValueHelperText('');
    }
  };

  const handleSaveValue = async () => {
    if (!backlogItemId) return;
    setError(null);
    setSuccessMessage(null);
    if (valueInput === null || valueInput < VALUE_MIN || valueInput > VALUE_MAX) {
      setValueError(true);
      setValueHelperText(`${VALUE_MIN}〜${VALUE_MAX}の範囲で入力してください`);
      return;
    }
    try {
      const updated = await backlogItemService.updateBacklogItem(backlogItemId, { value: valueInput });
      setItem(updated);
      setSuccessMessage('Valueを保存しました。');
    } catch (err: any) {
      console.error('Failed to save value:', err);
      setError(err?.response?.data?.message ?? err?.message ?? 'Valueの保存に失敗しました。');
    }
  };

  if (!item) {
    return <Typography>読み込み中...</Typography>;
  }

  return (
    <Box>
      <Button
        sx={{mb: 2}}
        onClick={() =>
          navigate(`/products/${productId}/scrum-teams/${scrumTeamId}/backlog-items`)
        }
      >
        ← 一覧へ戻る
      </Button>

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

      <Paper sx={{p: 3, mb: 3}}>
        <Box sx={{display: 'flex', alignItems: 'center', gap: 2, mb: 2}}>
          <Typography variant="h5">{item.title}</Typography>
          <Chip
            label={item.statusLabel || STATUS_LABELS[item.status]}
            color={STATUS_COLORS[item.status]}
            size="small"
          />
        </Box>
        <Typography variant="body1" sx={{whiteSpace: 'pre-wrap', mb: 2}}>
          {item.description || '（説明なし）'}
        </Typography>
        <Box
          sx={{
            mt: 2,
            mb: 2,
            display: 'flex',
            flexDirection: { xs: 'column', sm: 'row' },
            alignItems: { xs: 'stretch', sm: 'flex-end' },
            gap: 2,
          }}
        >
          <Box sx={{ flex: 1 }}>
            <Typography variant="body2" gutterBottom>
              Value *
            </Typography>
            <Slider
              value={valueInput ?? VALUE_MIN}
              onChange={handleSliderChange}
              min={VALUE_MIN}
              max={VALUE_MAX}
              marks={[
                { value: 0, label: '0' },
                { value: 25, label: '25' },
                { value: 50, label: '50' },
                { value: 75, label: '75' },
                { value: 100, label: '100' },
              ]}
              getAriaValueText={(val) => String(val)}
              valueLabelDisplay="auto"
              sx={{
                '& .MuiSlider-track': {
                  backgroundColor: valueInput != null ? valueToColor(valueInput) : valueToColor(VALUE_MIN),
                },
              }}
            />
            <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
              <TextField
                label="Value"
                type="number"
                fullWidth
                inputProps={{ min: VALUE_MIN, max: VALUE_MAX }}
                value={textFieldRaw}
                onChange={handleTextFieldChange}
                error={valueError}
                helperText={valueError ? valueHelperText : `範囲: ${VALUE_MIN}〜${VALUE_MAX}`}
                size="small"
                data-testid="value-textfield"
              />
            </Box>
          </Box>
          <Button
            variant="contained"
            onClick={handleSaveValue}
            disabled={valueInput === null || valueError}
          >
            Valueを保存
          </Button>
        </Box>
        <Typography variant="body2" color="text.secondary">
          優先度：{item.priority ?? '-'} ／ 作成者：{item.creatorName} ／ 作成日時：
          {new Date(item.createdAt).toLocaleString()}
          {item.sprintId ? ` ／ 関連スプリント：${item.sprintId}` : ''}
        </Typography>
      </Paper>

      <Paper sx={{p: 3}}>
        <Typography variant="h6" sx={{mb: 1}}>
          コメント
        </Typography>
        <List>
          {comments.map((comment) => (
            <React.Fragment key={comment.id}>
              <ListItem alignItems="flex-start">
                <ListItemText
                  primary={`${comment.authorName}（${new Date(comment.createdAt).toLocaleString()}）`}
                  secondary={comment.body}
                />
              </ListItem>
              <Divider component="li" />
            </React.Fragment>
          ))}
          {comments.length === 0 && (
            <ListItem>
              <ListItemText secondary="コメントはまだありません。" />
            </ListItem>
          )}
        </List>

        <Box sx={{mt: 2}}>
          <TextField
            label="投稿者名"
            fullWidth
            required
            margin="normal"
            value={commentAuthorName}
            onChange={(e) => setCommentAuthorName(e.target.value)}
          />
          <TextField
            label="コメント"
            fullWidth
            required
            multiline
            rows={3}
            margin="normal"
            value={commentBody}
            onChange={(e) => setCommentBody(e.target.value)}
          />
          <Button
            variant="contained"
            onClick={handleAddComment}
            disabled={!commentBody || !commentAuthorName}
          >
            コメントを追加
          </Button>
        </Box>
      </Paper>
    </Box>
  );
};

export default BacklogItemDetail;
