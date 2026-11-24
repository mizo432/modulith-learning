import React, { useEffect, useState } from 'react';
import { Box, LinearProgress, Typography } from '@mui/material';
import sprintService, { SprintCapacity } from '../services/sprintService';

interface SprintCapacityCellProps {
  sprintId: string;
}

/**
 * スプリントの容量（割り当て済み / 割り当て可能ポイント）を表示するセル。
 * 割り当て可能ポイントはチームのベロシティから算出される（#528）。
 */
const SprintCapacityCell: React.FC<SprintCapacityCellProps> = ({ sprintId }) => {
  const [capacity, setCapacity] = useState<SprintCapacity | null>(null);
  const [failed, setFailed] = useState(false);

  useEffect(() => {
    let cancelled = false;
    sprintService
      .getSprintCapacity(sprintId)
      .then((result) => {
        if (!cancelled) {
          setCapacity(result);
        }
      })
      .catch(() => {
        if (!cancelled) {
          setFailed(true);
        }
      });
    return () => {
      cancelled = true;
    };
  }, [sprintId]);

  if (failed) {
    return <Typography variant="body2">-</Typography>;
  }
  if (!capacity) {
    return <Typography variant="body2">…</Typography>;
  }

  const percent =
    capacity.assignablePoints > 0
      ? Math.min(100, (capacity.assignedPoints / capacity.assignablePoints) * 100)
      : capacity.assignedPoints > 0
        ? 100
        : 0;

  return (
    <Box sx={{ minWidth: 120 }}>
      <Typography variant="body2">
        {capacity.assignedPoints} / {capacity.assignablePoints} SP
      </Typography>
      <LinearProgress
        variant="determinate"
        value={percent}
        color={capacity.overAllocated ? 'error' : 'primary'}
        sx={{ mt: 0.5 }}
      />
      {capacity.overAllocated && (
        <Typography variant="caption" color="error">
          割り当て可能ポイントを超えています
        </Typography>
      )}
    </Box>
  );
};

export default SprintCapacityCell;
