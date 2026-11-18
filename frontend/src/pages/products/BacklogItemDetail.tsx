import React, {useCallback, useEffect, useRef, useState} from 'react';
import {useNavigate, useParams} from 'react-router-dom';
import {
  Alert,
  Box,
  Button,
  Chip,
  Divider,
  IconButton,
  List,
  ListItem,
  ListItemText,
  Paper,
  Slider,
  TextField,
  Typography,
} from '@mui/material';
import ReplyIcon from '@mui/icons-material/Reply';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import backlogItemService, {
  BacklogItem,
  BacklogItemComment,
  BacklogItemStatus,
} from '../../services/backlogItemService';
import {valueToColor, VALUE_MIN, VALUE_MAX} from '../../utils/valueColor';
import {useIsVisible} from '../../hooks/useIsVisible';
import {useAutoRefresh} from '../../hooks/useAutoRefresh';
import {useAuth} from '../../contexts/AuthContext';

const REFRESH_INTERVAL = 30000; // 30秒

const STATUS_LABELS: Record<BacklogItemStatus, string> = {
  SUGGESTED: '提案',
  ACCEPTED: '承認済み',
  FROZEN: '凍結',
};

const STATUS_COLORS: Record<BacklogItemStatus, 'warning' | 'success' | 'info'> = {
  SUGGESTED: 'warning',
  ACCEPTED: 'success',
  FROZEN: 'info',
};

const BacklogItemDetail: React.FC = () => {
  const {productId, scrumTeamId, backlogItemId} = useParams<{
    productId: string;
    scrumTeamId: string;
    backlogItemId: string;
  }>();
  const navigate = useNavigate();
  const auth = useAuth();

  const [item, setItem] = useState<BacklogItem | null>(null);
  const [comments, setComments] = useState<BacklogItemComment[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [successMessage, setSuccessMessage] = useState<string | null>(null);
  const [commentBody, setCommentBody] = useState('');
  const [commentAuthorName, setCommentAuthorName] = useState(
    auth.username ?? '',
  );

  // 返信入力状態
  const [replyingTo, setReplyingTo] = useState<string | null>(null);
  const [replyBody, setReplyBody] = useState('');

  // 編集状態
  const [editingCommentId, setEditingCommentId] = useState<string | null>(null);
  const [editBody, setEditBody] = useState('');

  // Value入力（数値型でSliderとTextFieldを双方向連動）
  const [valueInput, setValueInput] = useState<number | null>(null);
  const [valueError, setValueError] = useState(false);
  const [valueHelperText, setValueHelperText] = useState('');
  const [textFieldRaw, setTextFieldRaw] = useState('');

  // 入力状態の参照（load 関数内で最新値を取得するため）
  const valueInputRef = useRef(valueInput);
  valueInputRef.current = valueInput;
  const itemValueRef = useRef(item?.value);
  itemValueRef.current = item?.value;

  const load = useCallback(async () => {
    if (!backlogItemId) return;
    try {
      const [itemData, commentData] = await Promise.all([
        backlogItemService.getBacklogItem(backlogItemId),
        backlogItemService.getComments(backlogItemId),
      ]);
      setItem(itemData);
      setComments(commentData);
      // 入力中（commentBody が空でない、または valueInput が現在の値と異なる）場合は入力内容を上書きしない
      const isEditingValue = valueInputRef.current !== null && itemValueRef.current !== valueInputRef.current;
      if (!isEditingValue) {
        setValueInput(itemData.value != null ? itemData.value : null);
        setTextFieldRaw(itemData.value != null ? String(itemData.value) : '');
      }
    } catch (err) {
      console.error('Failed to load backlog item:', err);
      setError('バックログアイテムの読み込みに失敗しました。');
    }
  }, [backlogItemId]);

  useEffect(() => {
    load();
  }, [load]);

  // 自動リフレッシュ
  const {isVisible} = useIsVisible();
  // コメント入力中または編集中、または Value 編集中はスキップ
  const isCommentInputActive = commentBody !== '' || replyingTo !== null || editingCommentId !== null || replyBody !== '';
  const shouldSkipRefresh = isCommentInputActive || (valueInput !== null && item?.value !== valueInput);
  useAutoRefresh(load, REFRESH_INTERVAL, shouldSkipRefresh, isVisible);

  const handleAddComment = async (parentCommentId?: string) => {
    if (!backlogItemId) return;
    setError(null);
    try {
      const body = parentCommentId ? replyBody : commentBody;
      const request: { authorName: string; body: string; parentCommentId?: string; authorEmployeeId?: string } = {
        authorName: commentAuthorName,
        body,
      };
      if (parentCommentId) {
        request.parentCommentId = parentCommentId;
      } else {
        request.authorEmployeeId = auth.employeeId ?? undefined;
      }
      const created = await backlogItemService.addComment(backlogItemId, request);
      setComments((prev) => [...prev, created]);
      if (parentCommentId) {
        setReplyBody('');
        setReplyingTo(null);
      } else {
        setCommentBody('');
      }
    } catch (err: any) {
      console.error('Failed to add comment:', err);
      setError(err?.response?.data?.message ?? 'コメントの追加に失敗しました。');
    }
  };

  const handleUpdateComment = async (commentId: string) => {
    if (!backlogItemId) return;
    setError(null);
    try {
      const updated = await backlogItemService.updateComment(backlogItemId, commentId, {
        actorEmployeeId: auth.employeeId ?? undefined,
        body: editBody,
      });
      setComments((prev) =>
        prev.map((c) => (c.id === commentId ? updated : c)),
      );
      setEditingCommentId(null);
      setEditBody('');
    } catch (err: any) {
      console.error('Failed to update comment:', err);
      setError(err?.response?.data?.message ?? 'コメントの更新に失敗しました。');
    }
  };

  const handleDeleteComment = async (commentId: string) => {
    if (!backlogItemId) return;
    setError(null);
    try {
      await backlogItemService.deleteComment(backlogItemId, commentId, {
        actorEmployeeId: auth.employeeId ?? undefined,
      });
      setComments((prev) => prev.filter((c) => c.id !== commentId));
    } catch (err: any) {
      console.error('Failed to delete comment:', err);
      setError(err?.response?.data?.message ?? 'コメントの削除に失敗しました。');
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
            sx={{display: editingCommentId ? 'none' : 'inline-flex'}}
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
          {comments.length === 0 && (
            <ListItem>
              <ListItemText secondary="コメントはまだありません。" />
            </ListItem>
          )}
          {/* ルートコメント（parentIdがnull）のみをトップレベルで表示 */}
          {comments
            .filter((c) => c.parentId == null)
            .map((rootComment) => (
              <React.Fragment key={rootComment.id}>
                <CommentItem
                  comment={rootComment}
                  allComments={comments}
                  replyingTo={replyingTo}
                  replyBody={replyBody}
                  editingCommentId={editingCommentId}
                  editBody={editBody}
                  onReplyToggle={() =>
                    setReplyingTo(replyingTo === rootComment.id ? null : rootComment.id)
                  }
                  onReplyBodyChange={setReplyBody}
                  onReplySubmit={() => handleAddComment(rootComment.id)}
                  onEditToggle={() => {
                    if (editingCommentId === rootComment.id) {
                      setEditingCommentId(null);
                    } else {
                      setEditingCommentId(rootComment.id);
                      setEditBody(rootComment.body);
                    }
                  }}
                  onEditBodyChange={setEditBody}
                  onEditSave={() => handleUpdateComment(rootComment.id)}
                  onDelete={() => handleDeleteComment(rootComment.id)}
                />
                {/* 子コメント（返信）を再帰的に表示 */}
                <RepliesList
                  parentId={rootComment.id}
                  allComments={comments}
                  replyingTo={replyingTo}
                  replyBody={replyBody}
                  editingCommentId={editingCommentId}
                  editBody={editBody}
                  onReplyToggle={(id) => setReplyingTo(replyingTo === id ? null : id)}
                  onReplyBodyChange={setReplyBody}
                  onReplySubmit={(id) => handleAddComment(id)}
                  onEditToggle={(id) => {
                    if (editingCommentId === id) {
                      setEditingCommentId(null);
                    } else {
                      setEditingCommentId(id);
                      const comment = comments.find((c) => c.id === id);
                      setEditBody(comment?.body ?? '');
                    }
                  }}
                  onEditBodyChange={setEditBody}
                  onEditSave={(id) => handleUpdateComment(id)}
                  onDelete={(id) => handleDeleteComment(id)}
                />
              </React.Fragment>
            ))}
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
            data-testid="comment-textarea"
          />
          <Button
            variant="contained"
            onClick={() => handleAddComment()}
            disabled={!commentBody || !commentAuthorName}
          >
            コメントを追加
          </Button>
        </Box>
      </Paper>
    </Box>
  );
};

/* --- コメント関連のサブコンポーネント --- */

interface CommentItemProps {
  comment: BacklogItemComment;
  allComments: BacklogItemComment[];
  replyingTo: string | null;
  replyBody: string;
  editingCommentId: string | null;
  editBody: string;
  onReplyToggle: () => void;
  onReplyBodyChange: (body: string) => void;
  onReplySubmit: () => void;
  onEditToggle: () => void;
  onEditBodyChange: (body: string) => void;
  onEditSave: () => void;
  onDelete: () => void;
}

const CommentItem: React.FC<CommentItemProps> = ({
  comment,
  replyingTo,
  replyBody,
  editingCommentId,
  editBody,
  onReplyToggle,
  onReplyBodyChange,
  onReplySubmit,
  onEditToggle,
  onEditBodyChange,
  onEditSave,
  onDelete,
}) => {
  const isEditing = editingCommentId === comment.id;
  const isReplying = replyingTo === comment.id;
  const hasReplies = comment.hasReplies ?? false;

  return (
    <React.Fragment>
      <ListItem alignItems="flex-start">
        <ListItemText
          primary={`${comment.authorName}（${new Date(comment.createdAt).toLocaleString()}）`}
          secondary={
            isEditing ? (
              <Box sx={{mt: 1}}>
                <TextField
                  multiline
                  rows={3}
                  fullWidth
                  value={editBody}
                  onChange={(e) => onEditBodyChange(e.target.value)}
                  data-testid="edit-comment-textarea"
                  size="small"
                />
                <Box sx={{mt: 1, display: 'flex', gap: 1}}>
                  <Button
                    size="small"
                    variant="contained"
                    onClick={onEditSave}
                    data-testid="edit-save-button"
                    aria-label="コメント保存"
                  >
                    保存
                  </Button>
                  <Button size="small" onClick={onEditToggle}>
                    キャンセル
                  </Button>
                </Box>
              </Box>
            ) : (
              comment.body
            )
          }
        />
        <Box sx={{display: 'flex', gap: 0.5}}>
          <IconButton size="small" onClick={onReplyToggle} aria-label="返信">
            <ReplyIcon fontSize="small" />
          </IconButton>
          {!hasReplies && !isEditing && (
            <>
              <IconButton size="small" onClick={onEditToggle} aria-label="編集">
                <EditIcon fontSize="small" />
              </IconButton>
              <IconButton size="small" onClick={onDelete} aria-label="削除">
                <DeleteIcon fontSize="small" />
              </IconButton>
            </>
          )}
        </Box>
      </ListItem>
      {isReplying && (
        <ListItem sx={{pl: 4}}>
          <Box sx={{width: '100%'}}>
            <TextField
              label="返信"
              multiline
              rows={2}
              fullWidth
              value={replyBody}
              onChange={(e) => onReplyBodyChange(e.target.value)}
              data-testid="reply-textarea"
              size="small"
            />
            <Box sx={{mt: 1, display: 'flex', gap: 1}}>
              <Button
                size="small"
                variant="contained"
                onClick={onReplySubmit}
                disabled={!replyBody.trim()}
              >
                送信
              </Button>
              <Button size="small" onClick={onReplyToggle}>
                キャンセル
              </Button>
            </Box>
          </Box>
        </ListItem>
      )}
      <Divider component="li" />
    </React.Fragment>
  );
};

interface RepliesListProps {
  parentId: string;
  allComments: BacklogItemComment[];
  replyingTo: string | null;
  replyBody: string;
  editingCommentId: string | null;
  editBody: string;
  onReplyToggle: (id: string) => void;
  onReplyBodyChange: (body: string) => void;
  onReplySubmit: (id: string) => void;
  onEditToggle: (id: string) => void;
  onEditBodyChange: (body: string) => void;
  onEditSave: (id: string) => void;
  onDelete: (id: string) => void;
}

const RepliesList: React.FC<RepliesListProps> = ({
  parentId,
  allComments,
  replyingTo,
  replyBody,
  editingCommentId,
  editBody,
  onReplyToggle,
  onReplyBodyChange,
  onReplySubmit,
  onEditToggle,
  onEditBodyChange,
  onEditSave,
  onDelete,
}) => {
  const replies = allComments.filter((c) => c.parentId === parentId);
  if (replies.length === 0) return null;

  return (
    <>
      {replies.map((reply) => (
        <React.Fragment key={reply.id}>
          <ListItem alignItems="flex-start" sx={{pl: 4}}>
            <ListItemText
              primary={`${reply.authorName}（${new Date(reply.createdAt).toLocaleString()}）`}
              secondary={
                editingCommentId === reply.id ? (
                  <Box sx={{mt: 1}}>
                    <TextField
                      multiline
                      rows={2}
                      fullWidth
                      value={editBody}
                      onChange={(e) => onEditBodyChange(e.target.value)}
                      data-testid="edit-comment-textarea"
                      size="small"
                    />
                    <Box sx={{mt: 1, display: 'flex', gap: 1}}>
                      <Button
                        size="small"
                        variant="contained"
                        onClick={() => onEditSave(reply.id)}
                        data-testid="edit-save-button"
                      >
                        保存
                      </Button>
                      <Button size="small" onClick={() => onEditToggle(reply.id)}>
                        キャンセル
                      </Button>
                    </Box>
                  </Box>
                ) : (
                  reply.body
                )
              }
            />
            <Box sx={{display: 'flex', gap: 0.5}}>
              <IconButton size="small" onClick={() => onReplyToggle(reply.id)} aria-label="返信">
                <ReplyIcon fontSize="small" />
              </IconButton>
              {!(reply.hasReplies ?? false) && editingCommentId !== reply.id && (
                <>
                  <IconButton size="small" onClick={() => onEditToggle(reply.id)} aria-label="編集">
                    <EditIcon fontSize="small" />
                  </IconButton>
                  <IconButton size="small" onClick={() => onDelete(reply.id)} aria-label="削除">
                    <DeleteIcon fontSize="small" />
                  </IconButton>
                </>
              )}
            </Box>
          </ListItem>
          {replyingTo === reply.id && (
            <ListItem sx={{pl: 8}}>
              <Box sx={{width: '100%'}}>
                <TextField
                  label="返信"
                  multiline
                  rows={2}
                  fullWidth
                  value={replyBody}
                  onChange={(e) => onReplyBodyChange(e.target.value)}
                  data-testid="reply-textarea"
                  size="small"
                />
                <Box sx={{mt: 1, display: 'flex', gap: 1}}>
                  <Button
                    size="small"
                    variant="contained"
                    onClick={() => onReplySubmit(reply.id)}
                    disabled={!replyBody.trim()}
                    aria-label="送信"
                  >
                    送信
                  </Button>
                  <Button size="small" onClick={() => onReplyToggle(reply.id)}>
                    キャンセル
                  </Button>
                </Box>
              </Box>
            </ListItem>
          )}
          <Divider component="li" />
          {/* 再帰的に深いネストを表示 */}
          <RepliesList
            parentId={reply.id}
            allComments={allComments}
            replyingTo={replyingTo}
            replyBody={replyBody}
            editingCommentId={editingCommentId}
            editBody={editBody}
            onReplyToggle={onReplyToggle}
            onReplyBodyChange={onReplyBodyChange}
            onReplySubmit={onReplySubmit}
            onEditToggle={onEditToggle}
            onEditBodyChange={onEditBodyChange}
            onEditSave={onEditSave}
            onDelete={onDelete}
          />
        </React.Fragment>
      ))}
    </>
  );
};

export default BacklogItemDetail;
