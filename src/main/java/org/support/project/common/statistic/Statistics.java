package org.support.project.common.statistic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * データ分析用のクラス
 * 
 * @author koda
 * 
 */
public class Statistics {
	private static final Double MISSG = null;

	/**
	 * 与えられた配列の最小値を取得
	 * 
	 * @param data
	 * @return
	 */
	public static Double min(Double[] nums) {
		Double min = null;
		if (nums == null) {
			return null;
		}

		for (Double num : nums) {
			if (min == null) {
				min = num;
			} else if (min.compareTo(num) == 1) {
				min = num;
			}
		}
		return new Double(min.doubleValue());
	}

	/**
	 * 与えられた配列の最大値を取得
	 * 
	 * @param data
	 * @return
	 */
	public static Double max(Double[] nums) {
		Double max = null;
		if (nums == null) {
			return null;
		}

		for (Double num : nums) {
			if (max == null) {
				max = num;
			} else if (max.compareTo(num) == -1) {
				max = num;
			}
		}
		return new Double(max.doubleValue());
	}

	/**
	 * 与えられた配列の合計を取得
	 * 
	 * @param data
	 * @return
	 */
	public static Double sum(Double[] nums) {
		Double total = null;
		if (nums == null) {
			return null;
		}

		for (Double num : nums) {
			if (total == null) {
				total = num;
			} else {
				total += num;
			}
		}
		return new Double(total.doubleValue());
	}

	/**
	 * 与えられた配列の積を取得
	 * 
	 * @param data
	 * @return
	 */
	public static Double product(Double[] nums) {
		Double total = null;
		if (nums == null) {
			return null;
		}

		for (Double num : nums) {
			if (total == null) {
				total = num;
			} else {
				total *= num;
			}
		}
		return new Double(total.doubleValue());
	}

	/**
	 * 与えられた配列のカウントを取得
	 * 
	 * @param data
	 * @return
	 */
	public static Integer count(Double[] nums) {
		if (nums == null) {
			return null;
		}
		return nums.length;
	}

	/**
	 * 与えられた配列の相加平均(ArithmeticMean)、算術平均を取得
	 * 
	 * @param data
	 * @return
	 */
	public static Double arimean(Double[] nums) {
		if (nums == null) {
			return null;
		}
		Double total = 0.00;
		for (Double double1 : nums) {
			total += double1;
		}
		return total / nums.length;
	}

	/**
	 * 与えられた配列の相加平均(ArithmeticMean)、算術平均を取得
	 * 
	 * @param data
	 * @return
	 */
	public static Double avg(Double[] nums) {
		return arimean(nums);
	}

	/**
	 * 与えられた配列の幾何平均(GeometricMean)、相乗平均を取得
	 * 
	 * @param data
	 * @return
	 */
	public static Double geomean(Double[] nums) {
		if (nums == null) {
			return null;
		}
		int i, n = nums.length;
		Double product = 1.00;
		int count = 0;

		for (i = 0; i < n; i++) {
			Double d = nums[i];
			product = product * d;
			count++;
		}

		if (count == 2) {
			return Math.sqrt(product);
		} else if (count == 3) {
			return Math.cbrt(product);
		} else {
			return Math.pow(product, 1.00 / count);
		}
	}

	/**
	 * 与えられた配列の調和平均(harmonic mean)を取得
	 * 
	 * @param nums
	 * @return
	 */
	public static Double harmean(Double[] nums) {
		if (nums == null) {
			return null;
		}
		Double total = 0.00;
		for (Double double1 : nums) {
			total += (1.00 / double1);
		}
		return nums.length / total;
	}

	/**
	 * 与えられた配列の加重平均(WeightedMean)を取得
	 * 
	 * @param data
	 * @return
	 */
	public static Double weimean(List<Map<String, Double>> nums, Map<String, Double> weights) {
		if (nums == null) {
			return null;
		}
		Iterator<String> iterator = weights.keySet().iterator();
		Double total = 0.00;
		Double count = 0.00;
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			Double weight = weights.get(key);

			for (Map<String, Double> map : nums) {
				Double num = map.get(key);

				total += num * weight;
				count += 1 * weight;
			}
		}
		return total / count;
	}

	/**
	 * 与えられた配列の分散(variance)を取得 (不偏分散) 標本に基づく、分散の予測値を返します。
	 * 
	 * @param data
	 * @return
	 */
	public static Double var(Double[] nums) {
		if (nums == null) {
			return null;
		}
		Double avg = arimean(nums);
		Double sum = 0.00;
		for (Double double1 : nums) {
			Double d = double1 - avg;
			d = d * d;
			sum += d;
		}
		return (sum / (nums.length - 1));
	}

	/**
	 * 与えられた配列の標準偏差(standard deviation)を取得 (ExcelではSTDEVA)
	 * 
	 * @param data
	 * @return
	 */
	public static Double stdev(Double[] data) {
		return Math.sqrt(var(data));
	}

	/**
	 * 与えられた配列の分散(variance)を取得 (標本分散) 母集団に基づく分散を返します。
	 * 
	 * @param data
	 * @return
	 */
	public static Double varp(Double[] nums) {
		if (nums == null) {
			return null;
		}
		Double avg = arimean(nums);
		Double sum = 0.00;
		for (Double double1 : nums) {
			Double d = double1 - avg;
			d = d * d;
			sum += d;
		}
		return (sum / (nums.length));
	}

	/**
	 * 与えられた配列の標準偏差(standard deviation)を取得 (ExcelではSTDEV.P)
	 * 
	 * @param data
	 * @return
	 */
	public static Double stdevp(Double[] data) {
		return Math.sqrt(varp(data));
	}

	/**
	 * 二つの配列間の共分散を計算します
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public static Double covariance(Double[] x, Double[] y) {
		Double avg1 = arimean(x);
		Double avg2 = arimean(y);

		Double covar = 0.00;
		for (int i = 0; i < x.length; i++) {
			Double d1 = x[i];
			Double d2 = y[i];
			d1 = d1 - avg1;
			d2 = d2 - avg2;
			covar = covar + (d1 * d2);
		}
		return covar / x.length;
	}

	/**
	 * 二つの配列間の相関を計算する
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public static Double correlation(Double[] x, Double[] y) {
		Double std1 = stdevp(x);
		Double std2 = stdevp(y);
		Double covariance = covariance(x, y);
		return covariance / (std1 * std2);
	}

	/**
	 * 与えられた配列の歪度（わいど, skewness）を計算します ExcelではSKEW
	 * 
	 * @param data
	 * @return
	 */
	public static Double skew(Double[] data) {
		Double avg = arimean(data);

		Double stdev = 0.00;
		Double count = 0.00;
		for (int i = 0; i < data.length; i++) {
			Double d = data[i];
			if (d != MISSG) {
				d = d - avg;
				stdev = stdev + d * d;
				count += 1.00;
			}
		}

		stdev = Math.sqrt(stdev / (count - 1));

		if (stdev.compareTo(0.00) == 0) {
			return MISSG;
		}
		double skew = 0.;
		for (int i = 0; i < data.length; i++) {
			Double d = data[i];
			if (d != MISSG) {
				d = d - avg;
				d = d / stdev;
				skew = skew + d * d * d;
			}
		}

		return skew * count / ((count - 1) * (count - 2));

	}

	/**
	 * 与えられた配列の尖度（せんど、Kurtosis)を計算します ExcelではCURT
	 * 
	 * @param data
	 * @return
	 */
	public static Double kurt(Double[] data) {
		Double avg = arimean(data);

		Double stdev = 0.00;
		Double count = 0.00;
		for (int i = 0; i < data.length; i++) {
			Double d = data[i];
			if (d != MISSG) {
				d = d - avg;
				stdev = stdev + d * d;
				count += 1.00;
			}
		}

		stdev = Math.sqrt(stdev / (count - 1));

		if (stdev.compareTo(0.00) == 0) {
			return MISSG;
		}

		double kurt = 0.;
		for (int i = 0; i < data.length; i++) {
			Double d = data[i];
			if (d != MISSG) {
				d = d - avg;
				d = d / stdev;
				kurt = kurt + d * d * d * d;
			}
		}

		kurt = kurt * count * (count + 1) / (count - 1) - 3 * (count - 1) * (count - 1);
		return kurt / ((count - 2) * (count - 3));
	}

	/**
	 * 指定の数の、与えられた配列に対する相対値のランクを計算します
	 * 
	 * @param value
	 * @param data
	 * @return
	 */
	public static Double rank(Double value, Double[] data) {
		int n = data.length;
		Double d;
		Integer rank;

		if (value == MISSG) {
			return MISSG;
		}

		List<Double> ddd = new ArrayList<>();

		int j = 0;
		for (int i = 0; i < n; i++) {
			d = data[i];
			if (d != MISSG) {
				ddd.add(d);
				j++;
			}
		}
		n = j;
		if (n == 0) {
			return MISSG;
		}

		if (n == 1) {
			if (ddd.get(0) > value) {
				return 2.;
			} else {
				return 1.;
			}
		}

		Collections.sort(ddd);

		rank = 1;
		while (ddd.get(n - rank) > value) {
			rank++;
			if (rank > n) {
				break;
			}
		}
		return (double) rank;
	}

	/**
	 * 配列の最頻値を計算します。 最頻が複数(同じ件数の値が複数)ある場合は、一番初めの数字になります。 全てが1件づつだった場合、nullとしておきます。
	 * 
	 * @param data
	 * @return
	 */
	public static Double mode(Double[] data) {
		List<Double> list = Arrays.asList(data);
		Collections.sort(list);

		Double before = null;
		Double mode = null;
		int count = 0;
		int modeCount = 0;
		for (Double double1 : list) {
			if (before == null) {
				before = double1;
				mode = double1;
				count = 1;
				modeCount = count;
			} else {
				if (before.compareTo(double1) == 0) {
					count++;
					if (count > modeCount) {
						mode = double1;
						modeCount = count;
					}
				} else {
					before = double1;
					count = 1;
				}
			}
		}
		if (modeCount > 1) {
			return mode;
		}
		return null;
	}

	/**
	 * 配列の中央値を計算します
	 * 
	 * @param data
	 * @return
	 */
	public static Double median(Double[] data) {
		int n = data.length;
		int midIndex;
		Double median;

		List<Double> list = Arrays.asList(data);
		Collections.sort(list);

		midIndex = n / 2;
		if (n % 2 == 0) {
			/* Average of the two middle numbers */
			median = (list.get(midIndex) + list.get(midIndex - 1)) / 2.00;
		} else {
			median = list.get(midIndex);
		}
		return median;
	}

	/**
	 * 配列のパーセンタイル値を計算します。
	 * 
	 * ExcelのPERCENTILE関数。 PERCENTILE関数は指定した配列の中で百分率で指定した率に位置する値を返します。
	 * 
	 * @param percent
	 * @param data
	 * @return
	 */
	public static Double percentile(Double percent, Double[] data) {
		List<Double> list = Arrays.asList(data);
		Collections.sort(list);

		if (percent.compareTo(0.00) == 0) {
			return list.get(0);
		}

		if (percent.compareTo(1.00) == 0) {
			return list.get(list.size() - 1);
		}

		Double kubun = 1.00 / (list.size() - 1);
		int count = 0;
		for (Double double1 : list) {
			Double p = kubun * count;
			if (p.compareTo(percent) == 0) {
				return double1;
			}
			if (p.compareTo(percent) == 1) {
				Double before = list.get(count - 1);
				Double current = double1;
				Double sa = current - before;

				Double val = before + (sa * (percent - (kubun * (count - 1))) / kubun);
				return val;
			}
			count++;
		}
		return null;
	}

	/**
	 * 配列の四分位を計算します。
	 * 
	 * quartに0、1、2、3、4の数値以外を指定するとNull
	 * ExcelのQUARTILE関数。 QUARTILE関数はデータから四分位数を抽出します。
	 * 
	 * 戻り値	戻り値を0から4の数値で指定
	 * 0	最小値
	 * 1	第1四分位数(25%)
	 * 2	第2四分位数　中位数(50%)
	 * 3	第3四分位数(75%)
	 * 4	最大値
	 * 
	 * @param percent
	 * @param data
	 * @return
	 */
	public static double quartile(int quart, Double[] data) {
		switch (quart) {
			case 0:
				return min(data);
			case 1:
				return percentile(0.25, data);
			case 2:
				return median(data);
			case 3:
				return percentile(0.75, data);
			case 4:
				return max(data);
			default:
				return MISSG;
		}
	}
}
