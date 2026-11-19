import React from 'react';
import ReactMarkdown from 'react-markdown';
import remarkGfm from 'remark-gfm';
import rehypeSanitize from 'rehype-sanitize';
import { Box, SxProps, Theme } from '@mui/material';

interface MarkdownCommentProps {
  content: string;
  sx?: SxProps<Theme>;
}

export const MarkdownComment: React.FC<MarkdownCommentProps> = ({ content, sx }) => {
  if (!content || !content.trim()) {
    return <Box sx={sx}>（本文なし）</Box>;
  }

  return (
    <Box
      sx={{
        '& p': { margin: '0 0 8px 0' },
        '& p:last-child': { margin: 0 },
        '& pre': {
          backgroundColor: '#f5f5f5',
          padding: '8px 12px',
          borderRadius: '4px',
          overflowX: 'auto',
        },
        '& code': {
          backgroundColor: '#f5f5f5',
          padding: '2px 4px',
          borderRadius: '2px',
          fontSize: '0.9em',
        },
        '& pre code': {
          backgroundColor: 'transparent',
          padding: 0,
        },
        '& table': {
          borderCollapse: 'collapse',
          width: '100%',
          marginBottom: '8px',
        },
        '& th, & td': {
          border: '1px solid #ddd',
          padding: '6px 12px',
        },
        '& th': {
          backgroundColor: '#f5f5f5',
        },
        '& ul, & ol': {
          paddingLeft: '24px',
          margin: '0 0 8px 0',
        },
        '& blockquote': {
          borderLeft: '4px solid #ddd',
          paddingLeft: '12px',
          color: '#666',
          margin: '0 0 8px 0',
        },
        ...sx,
      }}
    >
      <ReactMarkdown
        remarkPlugins={[remarkGfm]}
        rehypePlugins={[rehypeSanitize]}
      >
        {content}
      </ReactMarkdown>
    </Box>
  );
};
