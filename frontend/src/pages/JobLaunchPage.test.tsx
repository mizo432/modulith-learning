import React from 'react';
import {render, screen, waitFor} from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import JobLaunchPage from './JobLaunchPage';
import * as jobService from '../services/jobService';

jest.mock('../services/jobService');

const mockUseAuth = jest.fn();
jest.mock('../contexts/AuthContext', () => ({
  useAuth: () => mockUseAuth(),
}));

const mockedGetDefinitions = jobService.getJobDefinitions as jest.Mock;
const mockedGetExecutions = jobService.getJobExecutions as jest.Mock;
const mockedStartJob = jobService.startJob as jest.Mock;

const definitions = [
  {
    id: '1',
    jobCode: 'sampleJob',
    version: '1.0',
    parameterSpec: 'key=value形式で指定',
    maxConcurrentExecutions: 1,
    timeoutSeconds: 300,
    isActive: true,
  },
  {
    id: '2',
    jobCode: 'otherJob',
    version: '1.0',
    parameterSpec: 'パラメータなし',
    isActive: true,
  },
];

beforeEach(() => {
  jest.clearAllMocks();
  mockUseAuth.mockReturnValue({
    isAuthenticated: true,
    username: 'admin',
    isAdmin: true,
    isFirstLogin: false,
    employeeId: '100',
  });
  mockedGetDefinitions.mockResolvedValue(definitions);
  mockedGetExecutions.mockResolvedValue([]);
  mockedStartJob.mockResolvedValue(undefined);
});

test('定義一覧が表示され、ジョブを選択したときParameterSpecが表示される', async () => {
  render(<JobLaunchPage/>);

  const select = await screen.findByRole('combobox', {name: /ジョブ/});
  await userEvent.click(select);
  const option = await screen.findByRole('option', {name: 'sampleJob'});
  await userEvent.click(option);

  expect(await screen.findByText('key=value形式で指定')).toBeInTheDocument();
});

test('起動ボタンを押したとき、選択したジョブコード・パラメータ・actorEmployeeIdで起動APIが呼ばれる', async () => {
  render(<JobLaunchPage/>);

  const select = await screen.findByRole('combobox', {name: /ジョブ/});
  await userEvent.click(select);
  await userEvent.click(await screen.findByRole('option', {name: 'sampleJob'}));
  const parametersInput = await screen.findByLabelText(/実行パラメータ/);
  await userEvent.type(parametersInput, 'foo=bar');
  await userEvent.click(screen.getByRole('button', {name: '起動する'}));

  await waitFor(() => {
    expect(mockedStartJob).toHaveBeenCalledWith({
      jobCode: 'sampleJob',
      actorEmployeeId: '100',
      parameters: 'foo=bar',
    });
  });
});

test('起動に成功したとき、成功メッセージが表示され実行一覧が更新される', async () => {
  mockedGetExecutions
      .mockResolvedValueOnce([])
      .mockResolvedValueOnce([
        {id: '9', executionId: 'exec-9', jobCode: 'sampleJob', state: 'RUNNING'},
      ]);
  render(<JobLaunchPage/>);

  const select = await screen.findByRole('combobox', {name: /ジョブ/});
  await userEvent.click(select);
  await userEvent.click(await screen.findByRole('option', {name: 'sampleJob'}));
  await userEvent.click(screen.getByRole('button', {name: '起動する'}));

  expect(await screen.findByText('ジョブを起動しました')).toBeInTheDocument();
  expect(await screen.findByText('exec-9')).toBeInTheDocument();
  expect(mockedGetExecutions).toHaveBeenCalledTimes(2);
});

test('起動に失敗したとき、エラーメッセージが表示される', async () => {
  mockedStartJob.mockRejectedValue({
    response: {data: {message: '同時実行数の上限に達しています'}},
  });
  render(<JobLaunchPage/>);

  const select = await screen.findByRole('combobox', {name: /ジョブ/});
  await userEvent.click(select);
  await userEvent.click(await screen.findByRole('option', {name: 'sampleJob'}));
  await userEvent.click(screen.getByRole('button', {name: '起動する'}));

  expect(await screen.findByText('同時実行数の上限に達しています')).toBeInTheDocument();
});

test('実行一覧に状態がラベル付きで表示される', async () => {
  mockedGetExecutions.mockResolvedValue([
    {id: '1', executionId: 'exec-1', jobCode: 'sampleJob', state: 'SUCCESS'},
    {id: '2', executionId: 'exec-2', jobCode: 'otherJob', state: 'RUNNING'},
    {id: '3', executionId: 'exec-3', jobCode: 'sampleJob', state: 'FAILED'},
  ]);
  render(<JobLaunchPage/>);

  expect(await screen.findByText('成功')).toBeInTheDocument();
  expect(await screen.findByText('実行中')).toBeInTheDocument();
  expect(await screen.findByText('失敗')).toBeInTheDocument();
});
