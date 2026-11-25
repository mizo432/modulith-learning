import React, {useCallback, useEffect, useRef, useState} from 'react';
import {useNavigate, useParams} from 'react-router-dom';
import {
  Alert,
  Box,
  Button,
  Chip,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  Divider,
  FormControl,
  FormControlLabel,
  IconButton,
  InputLabel,
  List,
  ListItem,
  ListItemText,
  MenuItem,
  Paper,
  Radio,
  RadioGroup,
  Select,
  Slider,
  TextField,
  Tooltip,
  Typography,
} from '@mui/material';
import ReplyIcon from '@mui/icons-material/Reply';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import DownloadIcon from '@mui/icons-material/Download';
import backlogItemService, {
  AssignableSprint,
  BacklogItem,
  BacklogItemAttachment,
  BacklogItemComment,
  BacklogItemDependencies,
  BacklogItemStatus,
} from '../../services/backlogItemService';
import scrumTeamService, {ScrumTeam} from '../../services/scrumTeamService';
import sprintService from '../../services/sprintService';
import {valueToColor, VALUE_MIN, VALUE_MAX, valueToQualityCategory, qualityCategoryToLabel, qualityCategoryToTooltip, QUALITY_CATEGORY_STYLES} from '../../utils/valueColor';
import {STORY_POINT_VALUES, getStoryPointDescription} from '../../utils/storyPoints';
import {useIsVisible} from '../../hooks/useIsVisible';
import {useAutoRefresh} from '../../hooks/useAutoRefresh';
import {useAuth} from '../../contexts/AuthContext';
import {useToast} from '../../contexts/ToastContext';
import {MarkdownComment} from '../../components/MarkdownComment';

const REFRESH_INTERVAL = 30000; // 30秒

const formatFileSize = (bytes: number): string => {
  if (bytes < 1024) return `${bytes} B`;
  if (bytes < 1024 * 1024) return `${(bytes / 1024).toFixed(1)} KB`;
  return `${(bytes / (1024 * 1024)).toFixed(1)} MB`;
};

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

const BacklogItemDetail: React.FC = () => {
  const {productId, scrumTeamId, backlogItemId} = useParams<{
    productId: string;
    scrumTeamId: string;
    backlogItemId: string;
  }>();
  const navigate = useNavigate();
  const auth = useAuth();
  const {showToast} = useToast();

  const [item, setItem] = useState<BacklogItem | null>(null);
  const [team, setTeam] = useState<ScrumTeam | null>(null);
  const [comments, setComments] = useState<BacklogItemComment[]>([]);
  const [attachments, setAttachments] = useState<BacklogItemAttachment[]>([]);
  const [uploadDialogOpen, setUploadDialogOpen] = useState(false);
  const [revertDialogOpen, setRevertDialogOpen] = useState(false);
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
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

  // StoryPoint選択（Issue #516）
  const [selectedStoryPoint, setSelectedStoryPoint] = useState<number | null>(null);

  // スプリント割り当て（Issue #525）
  const [assignableSprints, setAssignableSprints] = useState<AssignableSprint[]>([]);
  const [selectedSprintId, setSelectedSprintId] = useState('');

  // 依存関係（Issue #542）
  const [dependencies, setDependencies] = useState<BacklogItemDependencies | null>(null);
  const [teamItems, setTeamItems] = useState<BacklogItem[]>([]);
  const [selectedDependencyId, setSelectedDependencyId] = useState('');

  // 入力状態の参照（load 関数内で最新値を取得するため）
  const valueInputRef = useRef(valueInput);
  valueInputRef.current = valueInput;
  const itemValueRef = useRef(item?.value);
  itemValueRef.current = item?.value;
  const selectedStoryPointRef = useRef(selectedStoryPoint);
  selectedStoryPointRef.current = selectedStoryPoint;
  const itemStoryPointRef = useRef(item?.storyPoint);
  itemStoryPointRef.current = item?.storyPoint;

  const load = useCallback(async () => {
    if (!backlogItemId) return;
    try {
      const [itemData, commentData] = await Promise.all([
        backlogItemService.getBacklogItem(backlogItemId),
        backlogItemService.getComments(backlogItemId),
      ]);
      setItem(itemData);
      setComments(commentData);
      // 依存関係（取得できない場合はセクションを空にする）
      try {
        const dependencyData = await backlogItemService.getDependencies(backlogItemId);
        setDependencies(dependencyData);
      } catch {
        setDependencies(null);
      }
      // 添付ファイル一覧（取得できない場合は詳細レスポンスに含まれる添付を使う）
      if (auth.employeeId) {
        try {
          const attachmentData = await backlogItemService.getAttachments(
            backlogItemId,
            auth.employeeId,
          );
          setAttachments(attachmentData);
        } catch {
          setAttachments(itemData.attachments ?? []);
        }
      } else {
        setAttachments(itemData.attachments ?? []);
      }
      // 入力中（commentBody が空でない、または valueInput が現在の値と異なる）場合は入力内容を上書きしない
      const isEditingValue = valueInputRef.current !== null && itemValueRef.current !== valueInputRef.current;
      if (!isEditingValue) {
        setValueInput(itemData.value != null ? itemData.value : null);
        setTextFieldRaw(itemData.value != null ? String(itemData.value) : '');
      }
      // StoryPoint入力中は上書きしない
      const isEditingStoryPoint = selectedStoryPointRef.current !== null && itemStoryPointRef.current !== selectedStoryPointRef.current;
      if (!isEditingStoryPoint) {
        setSelectedStoryPoint(itemData.storyPoint != null ? itemData.storyPoint : null);
      }
    } catch (err) {
      console.error('Failed to load backlog item:', err);
      setError('バックログアイテムの読み込みに失敗しました。');
    }
  }, [backlogItemId, auth.employeeId]);

  useEffect(() => {
    load();
  }, [load]);

  // チーム情報を取得してメンバー判定（Issue #516）
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

  // チームメンバーかどうかを判定（memberEmployeeIds に auth.employeeId が含まれる）
  const isTeamMember =
    !!auth.employeeId &&
    !!team?.memberEmployeeIds &&
    team.memberEmployeeIds.includes(auth.employeeId);

  // POまたはSMかどうかを判定（依存関係の追加・削除は PO・SM のみ。Issue #542）
  const isPoOrSm =
    !!auth.employeeId &&
    !!team &&
    (team.poEmployeeId === auth.employeeId || team.smEmployeeId === auth.employeeId);

  // 依存先の候補（同一チームのアイテム）を取得する
  useEffect(() => {
    if (!isPoOrSm || !scrumTeamId) return;
    backlogItemService
      .getBacklogItemsByTeam(scrumTeamId)
      .then(setTeamItems)
      .catch(() => setTeamItems([]));
  }, [isPoOrSm, scrumTeamId]);

  // 自動リフレッシュ
  const {isVisible} = useIsVisible();
  // コメント入力中または編集中、または Value 編集中、または StoryPoint 編集中はスキップ
  const isCommentInputActive = commentBody !== '' || replyingTo !== null || editingCommentId !== null || replyBody !== '';
  const isEditingStoryPoint = selectedStoryPoint !== null && item?.storyPoint !== selectedStoryPoint;
  const shouldSkipRefresh = isCommentInputActive || (valueInput !== null && item?.value !== valueInput) || isEditingStoryPoint;
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

  const handleUploadAttachment = async () => {
    if (!backlogItemId || !selectedFile) return;
    setError(null);
    setSuccessMessage(null);
    try {
      await backlogItemService.uploadAttachment(
        backlogItemId,
        selectedFile,
        auth.employeeId ?? undefined,
        auth.username ?? undefined,
      );
      setUploadDialogOpen(false);
      setSelectedFile(null);
      setSuccessMessage('ファイルを添付しました。');
      await load();
    } catch (err: any) {
      console.error('Failed to upload attachment:', err);
      setError(err?.response?.data?.message ?? '添付ファイルのアップロードに失敗しました。');
    }
  };

  const handleDeleteAttachment = async (attachment: BacklogItemAttachment) => {
    if (!backlogItemId || !auth.employeeId) return;
    setError(null);
    setSuccessMessage(null);
    try {
      await backlogItemService.deleteAttachment(
        backlogItemId,
        attachment.documentId,
        auth.employeeId,
      );
      setSuccessMessage('添付ファイルを削除しました。');
      await load();
    } catch (err: any) {
      console.error('Failed to delete attachment:', err);
      setError(err?.response?.data?.message ?? '添付ファイルの削除に失敗しました。');
    }
  };

  const handleDownloadAttachment = async (attachment: BacklogItemAttachment) => {
    if (!backlogItemId || !auth.employeeId) return;
    setError(null);
    try {
      const blob = await backlogItemService.downloadAttachment(
        backlogItemId,
        attachment.documentId,
        auth.employeeId,
      );
      const url = URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.download = attachment.fileName;
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      URL.revokeObjectURL(url);
    } catch (err: any) {
      console.error('Failed to download attachment:', err);
      setError(err?.response?.data?.message ?? '添付ファイルのダウンロードに失敗しました。');
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

  // StoryPoint見積もり保存（Issue #516）
  const handleSaveStoryPoint = async () => {
    if (!backlogItemId || selectedStoryPoint === null) return;
    setError(null);
    setSuccessMessage(null);
    try {
      await backlogItemService.estimateBacklogItem(backlogItemId, {
        storyPoint: selectedStoryPoint,
        actorEmployeeId: auth.employeeId ?? undefined,
      });
      setSuccessMessage('StoryPointを保存しました。');
      // 再取得して状態変化を反映
      await load();
    } catch (err: any) {
      console.error('Failed to save story point:', err);
      setError(err?.response?.data?.message ?? err?.message ?? 'StoryPointの保存に失敗しました。');
    }
  };

  // Ready状態へ遷移（Issue #714）
  const handleReady = async () => {
    if (!backlogItemId) return;
    setError(null);
    setSuccessMessage(null);
    try {
      await backlogItemService.changeStatus(backlogItemId, 'READY', auth.employeeId ?? '');
      setSuccessMessage('準備完了状態に遷移しました。');
      // 再取得して状態変化を反映
      await load();
    } catch (err: any) {
      console.error('Failed to change status to Ready:', err);
      setError(err?.response?.data?.message ?? err?.message ?? '状態遷移に失敗しました。');
    }
  };

  // Accepted状態へ戻す（Issue #522）。ストーリーポイントはサーバー側でクリアされる
  const handleRevertToAccepted = async () => {
    if (!backlogItemId) return;
    setError(null);
    setSuccessMessage(null);
    try {
      await backlogItemService.changeStatus(backlogItemId, 'ACCEPTED', auth.employeeId ?? '');
      setRevertDialogOpen(false);
      setSuccessMessage('受入済み状態に戻しました。');
      // 再取得して状態変化を反映
      await load();
    } catch (err: any) {
      console.error('Failed to change status to Accepted:', err);
      setRevertDialogOpen(false);
      setError(err?.response?.data?.message ?? err?.message ?? '状態遷移に失敗しました。');
    }
  };

  // スプリント割り当てセクションの表示条件（ESTIMATED かつチームメンバーのみ。Issue #525）
  const showSprintAssignmentSection = isTeamMember && item?.status === 'ESTIMATED';

  // 割り当て可能なスプリント一覧の読み込み（Issue #525）
  useEffect(() => {
    if (!showSprintAssignmentSection || !backlogItemId || !auth.employeeId) return;
    let cancelled = false;
    backlogItemService
      .getAssignableSprints(backlogItemId, auth.employeeId)
      .then((sprints) => {
        if (cancelled) return;
        setAssignableSprints(sprints);
        const assigned = sprints.find((s) => s.assigned);
        setSelectedSprintId(assigned ? assigned.sprintId : '');
      })
      .catch((err) => {
        console.error('Failed to load assignable sprints:', err);
      });
    return () => {
      cancelled = true;
    };
  }, [showSprintAssignmentSection, backlogItemId, auth.employeeId, item?.sprintId]);

  // スプリントへ割り当て／変更（Issue #525）
  const handleAssignSprint = async () => {
    if (!backlogItemId || !selectedSprintId) return;
    setError(null);
    setSuccessMessage(null);
    try {
      await backlogItemService.assignSprint(backlogItemId, selectedSprintId, auth.employeeId ?? '');
      // 割り当て後の容量超過を警告トーストで知らせる（Issue #531/#534）。
      // 容量取得の失敗は割り当ての成功に影響させない
      try {
        const capacity = await sprintService.getSprintCapacity(selectedSprintId);
        if (capacity.overAllocated) {
          const excess = capacity.assignedPoints - capacity.assignablePoints;
          showToast(`割り当て可能ポイントを超えています（${excess}ポイント超過）`, 'warning');
        }
      } catch (capacityErr) {
        console.error('Failed to load sprint capacity:', capacityErr);
      }
      setSuccessMessage('スプリントへ割り当てました。');
      await load();
    } catch (err: any) {
      console.error('Failed to assign sprint:', err);
      setError(err?.response?.data?.message ?? err?.message ?? 'スプリント割り当てに失敗しました。');
    }
  };

  // スプリント割り当ての解除（Issue #525）
  const handleUnassignSprint = async () => {
    if (!backlogItemId) return;
    setError(null);
    setSuccessMessage(null);
    try {
      await backlogItemService.unassignSprint(backlogItemId, auth.employeeId ?? '');
      setSuccessMessage('スプリント割り当てを解除しました。');
      await load();
    } catch (err: any) {
      console.error('Failed to unassign sprint:', err);
      setError(err?.response?.data?.message ?? err?.message ?? 'スプリント割り当ての解除に失敗しました。');
    }
  };

  // 依存関係の追加（Issue #542）
  const handleAddDependency = async () => {
    if (!backlogItemId || !selectedDependencyId || !auth.employeeId) return;
    setError(null);
    try {
      const updated = await backlogItemService.addDependency(
        backlogItemId,
        selectedDependencyId,
        auth.employeeId,
      );
      setDependencies(updated);
      setSelectedDependencyId('');
      setSuccessMessage('依存関係を追加しました。');
    } catch (err: any) {
      console.error('Failed to add dependency:', err);
      setError(err?.response?.data?.message ?? '依存関係の追加に失敗しました。');
    }
  };

  // 依存関係の削除（Issue #542）
  const handleRemoveDependency = async (dependencyItemId: string) => {
    if (!backlogItemId || !auth.employeeId) return;
    setError(null);
    try {
      const updated = await backlogItemService.removeDependency(
        backlogItemId,
        dependencyItemId,
        auth.employeeId,
      );
      setDependencies(updated);
      setSuccessMessage('依存関係を削除しました。');
    } catch (err: any) {
      console.error('Failed to remove dependency:', err);
      setError(err?.response?.data?.message ?? '依存関係の削除に失敗しました。');
    }
  };

  // 依存先の候補（自分自身と追加済みを除く同一チームのアイテム）
  const dependencyCandidates = teamItems.filter(
    (candidate) =>
      candidate.id !== backlogItemId &&
      !dependencies?.dependencies.some((entry) => entry.backlogItemId === candidate.id),
  );

  const assignedSprint = assignableSprints.find((s) => s.assigned);

  // StoryPoint見積もりセクションの表示条件（READY または ESTIMATED のみ）
  const showStoryPointSection = item?.status === 'READY' || item?.status === 'ESTIMATED';

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
          {item.qualityCategory && (
            <Tooltip
              title={qualityCategoryToTooltip(item.qualityCategory)}
              arrow
              enterDelay={300}
            >
              <Chip
                label={item.qualityCategoryLabel || qualityCategoryToLabel(item.qualityCategory)}
                size="small"
                tabIndex={0}
                sx={{
                  backgroundColor: QUALITY_CATEGORY_STYLES[item.qualityCategory]?.backgroundColor,
                  color: QUALITY_CATEGORY_STYLES[item.qualityCategory]?.color,
                  fontWeight: 'medium',
                }}
              />
            </Tooltip>
          )}
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
              {(() => {
                const cat = valueToQualityCategory(valueInput);
                if (cat === null) return '―';
                return (
                  <Tooltip
                    title={qualityCategoryToTooltip(cat)}
                    arrow
                    enterDelay={300}
                  >
                    <Chip
                      label={qualityCategoryToLabel(cat)}
                      size="small"
                      tabIndex={0}
                      sx={{
                        backgroundColor: QUALITY_CATEGORY_STYLES[cat]?.backgroundColor,
                        color: QUALITY_CATEGORY_STYLES[cat]?.color,
                        fontWeight: 'medium',
                      }}
                    />
                  </Tooltip>
                );
              })()}
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

        {/* Readyボタン（Issue #714）：ACCEPTED状態かつチームメンバーのみ表示 */}
        {isTeamMember && item?.status === 'ACCEPTED' && (
          <Box sx={{mt: 2, display: 'flex', justifyContent: 'flex-end'}}>
            <Button
              variant="contained"
              onClick={handleReady}
            >
              Ready
            </Button>
          </Box>
        )}

        {/* Acceptedに戻すボタン（Issue #522）：READY状態かつチームメンバーのみ表示 */}
        {isTeamMember && item?.status === 'READY' && (
          <Box sx={{mt: 2, display: 'flex', justifyContent: 'flex-end'}}>
            <Button
              variant="outlined"
              color="warning"
              onClick={() => setRevertDialogOpen(true)}
            >
              Acceptedに戻す
            </Button>
          </Box>
        )}

        {/* StoryPoint見積もりセクション（Issue #516） */}
        {showStoryPointSection && (
          <Box sx={{mt: 3, pt: 2, borderTop: '1px solid #e0e0e0'}}>
            <Typography variant="subtitle1" gutterBottom>
              StoryPoint（見積もり）
            </Typography>
            <RadioGroup
              aria-label="StoryPoint選択"
              value={selectedStoryPoint != null ? String(selectedStoryPoint) : ''}
              onChange={(e) => setSelectedStoryPoint(Number(e.target.value))}
            >
              {STORY_POINT_VALUES.map((sp) => (
                <FormControlLabel
                  key={sp}
                  value={String(sp)}
                  control={<Radio size="small" />}
                  label={
                    <Box component="span" sx={{display: 'flex', alignItems: 'center', gap: 1}}>
                      <Typography component="span" variant="body2">{sp}</Typography>
                      <Typography component="span" variant="body2" color="text.secondary">
                        {getStoryPointDescription(sp)}
                      </Typography>
                    </Box>
                  }
                />
              ))}
            </RadioGroup>
            {/* チームメンバーのみ保存ボタンを表示（Issue #516） */}
            {isTeamMember && (
              <Box sx={{mt: 2}}>
                <Button
                  variant="contained"
                  onClick={handleSaveStoryPoint}
                  disabled={selectedStoryPoint === null}
                >
                  StoryPointを保存
                </Button>
              </Box>
            )}
          </Box>
        )}

        {/* スプリント割り当てセクション（Issue #525）：ESTIMATED かつチームメンバーのみ表示 */}
        {showSprintAssignmentSection && (
          <Box sx={{mt: 3, pt: 2, borderTop: '1px solid #e0e0e0'}}>
            <Typography variant="subtitle1" gutterBottom>
              スプリント割り当て
            </Typography>
            {assignedSprint && (
              <Typography variant="body2" sx={{mb: 1}}>
                現在の割り当て：Sprint {assignedSprint.sprintNumber}：{assignedSprint.title}
              </Typography>
            )}
            <Box sx={{display: 'flex', alignItems: 'center', gap: 2, flexWrap: 'wrap'}}>
              <FormControl size="small" sx={{minWidth: 240}}>
                <InputLabel id="sprint-select-label">スプリント</InputLabel>
                <Select
                  labelId="sprint-select-label"
                  label="スプリント"
                  data-testid="sprint-select"
                  value={selectedSprintId}
                  onChange={(e) => setSelectedSprintId(e.target.value)}
                >
                  {assignableSprints.map((sprint) => (
                    <MenuItem key={sprint.sprintId} value={sprint.sprintId}>
                      Sprint {sprint.sprintNumber}：{sprint.title}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
              <Button
                variant="contained"
                onClick={handleAssignSprint}
                disabled={!selectedSprintId || selectedSprintId === item.sprintId}
              >
                スプリントに割り当て
              </Button>
              {item.sprintId && (
                <Button variant="outlined" color="warning" onClick={handleUnassignSprint}>
                  割り当てを解除
                </Button>
              )}
            </Box>
          </Box>
        )}

        {/* 依存関係セクション（Issue #542）：リスト形式で表示し、追加・削除は PO・SM のみ */}
        <Box sx={{mt: 3, pt: 2, borderTop: '1px solid #e0e0e0'}}>
          <Typography variant="subtitle1" gutterBottom>
            依存関係
          </Typography>
          {dependencies && !dependencies.dependenciesSatisfied && (
            <Alert severity="warning" sx={{mb: 2}}>
              依存先が完了していないため、このアイテムには着手できません。
            </Alert>
          )}
          <Typography variant="body2" sx={{mt: 1}}>
            依存先
          </Typography>
          {dependencies && dependencies.dependencies.length > 0 ? (
            <List dense>
              {dependencies.dependencies.map((entry) => (
                <ListItem
                  key={entry.backlogItemId}
                  secondaryAction={
                    isPoOrSm ? (
                      <IconButton
                        edge="end"
                        aria-label="依存を削除"
                        data-testid={`remove-dependency-${entry.backlogItemId}`}
                        onClick={() => handleRemoveDependency(entry.backlogItemId)}
                      >
                        <DeleteIcon />
                      </IconButton>
                    ) : undefined
                  }
                >
                  <ListItemText primary={entry.title} />
                  <Chip
                    label={entry.statusLabel}
                    color={entry.satisfied ? 'success' : 'default'}
                    size="small"
                  />
                </ListItem>
              ))}
            </List>
          ) : (
            <Typography variant="body2" color="text.secondary">
              依存先はありません。
            </Typography>
          )}
          <Typography variant="body2" sx={{mt: 2}}>
            依存元
          </Typography>
          {dependencies && dependencies.dependents.length > 0 ? (
            <List dense>
              {dependencies.dependents.map((entry) => (
                <ListItem key={entry.backlogItemId}>
                  <ListItemText primary={entry.title} />
                  <Chip
                    label={entry.statusLabel}
                    color={entry.satisfied ? 'success' : 'default'}
                    size="small"
                  />
                </ListItem>
              ))}
            </List>
          ) : (
            <Typography variant="body2" color="text.secondary">
              依存元はありません。
            </Typography>
          )}
          {isPoOrSm && (
            <Box sx={{display: 'flex', alignItems: 'center', gap: 2, flexWrap: 'wrap', mt: 2}}>
              <FormControl size="small" sx={{minWidth: 240}}>
                <InputLabel id="dependency-select-label">依存先</InputLabel>
                <Select
                  labelId="dependency-select-label"
                  label="依存先"
                  data-testid="dependency-select"
                  value={selectedDependencyId}
                  onChange={(e) => setSelectedDependencyId(e.target.value)}
                >
                  {dependencyCandidates.map((candidate) => (
                    <MenuItem key={candidate.id} value={candidate.id}>
                      {candidate.title}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
              <Button
                variant="contained"
                onClick={handleAddDependency}
                disabled={!selectedDependencyId}
              >
                依存を追加
              </Button>
            </Box>
          )}
        </Box>

        <Typography variant="body2" color="text.secondary">
          優先度：{item.priority ?? '-'} ／ 作成者：{item.creatorName} ／ 作成日時：
          {new Date(item.createdAt).toLocaleString()}
          {item.sprintId ? ` ／ 関連スプリント：${item.sprintId}` : ''}
        </Typography>
      </Paper>

      <Paper sx={{p: 3, mb: 3}}>
        <Typography variant="h6" sx={{mb: 1}}>
          添付ファイル
        </Typography>
        <List>
          {attachments.length === 0 && (
            <ListItem>
              <ListItemText secondary="添付ファイルはまだありません。" />
            </ListItem>
          )}
          {attachments.map((attachment) => (
            <ListItem
              key={attachment.documentId}
              secondaryAction={
                <>
                  <IconButton
                    edge="end"
                    aria-label={`${attachment.fileName}をダウンロード`}
                    onClick={() => handleDownloadAttachment(attachment)}
                  >
                    <DownloadIcon />
                  </IconButton>
                  {auth.employeeId === attachment.uploaderEmployeeId && (
                    <IconButton
                      edge="end"
                      aria-label={`${attachment.fileName}を削除`}
                      onClick={() => handleDeleteAttachment(attachment)}
                    >
                      <DeleteIcon />
                    </IconButton>
                  )}
                </>
              }
            >
              <ListItemText
                primary={attachment.fileName}
                secondary={`${formatFileSize(attachment.fileSize)} ／ v${attachment.version} ／ ${attachment.uploaderName} ／ ${new Date(attachment.createdAt).toLocaleString()}`}
              />
            </ListItem>
          ))}
        </List>
        {isTeamMember && (
          <Box sx={{mt: 2}}>
            <Button variant="contained" onClick={() => setUploadDialogOpen(true)}>
              ファイルを添付
            </Button>
          </Box>
        )}
      </Paper>

      <Dialog open={revertDialogOpen} onClose={() => setRevertDialogOpen(false)}>
        <DialogTitle>Acceptedに戻しますか</DialogTitle>
        <DialogContent>
          <Typography>
            Acceptedに戻すと、設定されているストーリーポイントはクリアされます。再度見積もりが必要になります。
          </Typography>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setRevertDialogOpen(false)}>キャンセル</Button>
          <Button variant="contained" color="warning" onClick={handleRevertToAccepted}>
            戻す
          </Button>
        </DialogActions>
      </Dialog>

      <Dialog open={uploadDialogOpen} onClose={() => setUploadDialogOpen(false)}>
        <DialogTitle>ファイルを添付</DialogTitle>
        <DialogContent>
          <input
            type="file"
            data-testid="attachment-file-input"
            onChange={(e) => setSelectedFile(e.target.files?.[0] ?? null)}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setUploadDialogOpen(false)}>キャンセル</Button>
          <Button variant="contained" onClick={handleUploadAttachment} disabled={!selectedFile}>
            アップロード
          </Button>
        </DialogActions>
      </Dialog>

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
                  canDelete={auth.employeeId === rootComment.authorEmployeeId || auth.isAdmin}
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
                  employeeId={auth.employeeId}
                  isAdmin={auth.isAdmin}
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
            コメントを送信
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
  canDelete: boolean;
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
  canDelete,
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
              <MarkdownComment content={comment.body} />
            )
          }
        />
        <Box sx={{display: 'flex', gap: 0.5}}>
          <IconButton size="small" onClick={onReplyToggle} aria-label="返信">
            <ReplyIcon fontSize="small" />
          </IconButton>
          {canDelete && !hasReplies && !isEditing && (
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
  employeeId: string | null;
  isAdmin: boolean;
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
  employeeId,
  isAdmin,
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
                  <MarkdownComment content={reply.body} />
                )
              }
            />
            <Box sx={{display: 'flex', gap: 0.5}}>
              <IconButton size="small" onClick={() => onReplyToggle(reply.id)} aria-label="返信">
                <ReplyIcon fontSize="small" />
              </IconButton>
              {(employeeId === reply.authorEmployeeId || isAdmin) && !(reply.hasReplies ?? false) && editingCommentId !== reply.id && (
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
            employeeId={employeeId}
            isAdmin={isAdmin}
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
