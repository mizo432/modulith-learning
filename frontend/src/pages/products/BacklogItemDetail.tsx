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
  TextField,
  Typography,
} from '@mui/material';
import backlogItemService, {
  BacklogItem,
  BacklogItemComment,
  BacklogItemStatus,
} from '../../services/backlogItemService';

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
  const [commentBody, setCommentBody] = useState('');
  const [commentAuthorName, setCommentAuthorName] = useState(
    localStorage.getItem('user') ?? '',
  );

  const load = useCallback(async () => {
    if (!backlogItemId) return;
    try {
      const [itemData, commentData] = await Promise.all([
        backlogItemService.getBacklogItem(backlogItemId),
        backlogItemService.getComments(backlogItemId),
      ]);
      setItem(itemData);
      setComments(commentData);
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
