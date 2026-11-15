// Mock the api module before importing sprintService
const mockPost = jest.fn();
jest.mock('../../services/api', () => ({
  __esModule: true,
  default: {
    post: (...args: any[]) => mockPost(...args),
  },
}));

import sprintService, {
  CalculateEndDateRequest,
  CalculateEndDateResponse,
  END_DAY_OF_WEEK_LABELS,
} from '../../services/sprintService';

describe('sprintService', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  describe('calculateEndDate', () => {
    const mockRequest: CalculateEndDateRequest = {
      productId: 'test-product-id',
      startDate: '2026-11-16',
    };

    const mockResponse: CalculateEndDateResponse = {
      startDate: '2026-11-16',
      endDate: '2026-12-04',
      businessDays: 10,
      endDayOfWeekLabel: 'FRIDAY',
      isPastDate: false,
    };

    it('should return calculated end date', async () => {
      mockPost.mockResolvedValue({ data: mockResponse });

      const result = await sprintService.calculateEndDate(mockRequest);

      expect(mockPost).toHaveBeenCalledWith(
        '/v1/sprints/settings/calculate-end-date',
        mockRequest
      );
      expect(result).toEqual(mockResponse);
    });

    it('should return end date with past date flag', async () => {
      const pastDateResponse: CalculateEndDateResponse = {
        startDate: '2020-01-01',
        endDate: '2020-01-10',
        businessDays: 5,
        endDayOfWeekLabel: 'FRIDAY',
        isPastDate: true,
      };

      mockPost.mockResolvedValue({ data: pastDateResponse });

      const result = await sprintService.calculateEndDate({
        productId: 'test-product-id',
        startDate: '2020-01-01',
      });

      expect(result.isPastDate).toBe(true);
      expect(result.endDate).toBe('2020-01-10');
    });

    it('should handle different end days of week', async () => {
      const wednesdayResponse: CalculateEndDateResponse = {
        startDate: '2026-11-16',
        endDate: '2026-11-25',
        businessDays: 3,
        endDayOfWeekLabel: 'WEDNESDAY',
        isPastDate: false,
      };

      mockPost.mockResolvedValue({ data: wednesdayResponse });

      const result = await sprintService.calculateEndDate(mockRequest);

      expect(result.endDayOfWeekLabel).toBe('WEDNESDAY');
      expect(result.businessDays).toBe(3);
    });

    it('should propagate error when API fails', async () => {
      const error = new Error('Sprint settings not found');
      (error as any).response = {
        data: { message: 'Sprint settings not found' },
        status: 404,
      };

      mockPost.mockRejectedValue(error);

      await expect(sprintService.calculateEndDate(mockRequest)).rejects.toThrow('Sprint settings not found');
    });
  });

  describe('END_DAY_OF_WEEK_LABELS', () => {
    it('should have all weekday labels', () => {
      expect(END_DAY_OF_WEEK_LABELS.MONDAY).toBe('月曜日');
      expect(END_DAY_OF_WEEK_LABELS.TUESDAY).toBe('火曜日');
      expect(END_DAY_OF_WEEK_LABELS.WEDNESDAY).toBe('水曜日');
      expect(END_DAY_OF_WEEK_LABELS.THURSDAY).toBe('木曜日');
      expect(END_DAY_OF_WEEK_LABELS.FRIDAY).toBe('金曜日');
    });
  });
});
