package com.small.util;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * description:
 *
 * @author wesson
 * Create at 2021-07-30 21:07
 */
public class Point2DUtil {

    public static void main(String[] args) {
        final Point2D.Double aDouble = new Point2D.Double(1, 1.588888);
        final Point2D.Double aDouble2 = new Point2D.Double(1, 11.588888);
        final Point2D.Double aDouble3 = new Point2D.Double(2, 1.588888);
        final Point2D.Double aDouble4 = new Point2D.Double(1333, 1.588888);
        final Point2D.Double aDouble5 = new Point2D.Double(444, 123232.588888);
        final Point2D.Double a1 = new Point2D.Double(1, 1);
        final Point2D.Double a2 = new Point2D.Double(2, 1);
        final Point2D.Double a3 = new Point2D.Double(2, 2);
        final Point2D.Double a4 = new Point2D.Double(1, 2);

        List<Point2D.Double> pts = new ArrayList<>();
        pts.add(a1);
        pts.add(a2);
        pts.add(a3);
        pts.add(a4);
        final long l = System.nanoTime();
        System.out.println(isInPolygon(aDouble, pts));
        final long l2 = System.nanoTime();
        System.out.println("----" + (l2 - l));
        System.out.println(isInPolygon(aDouble2, pts));
        final long l3 = System.nanoTime();
        System.out.println("----" + (l3 - l2));
        System.out.println(isInPolygon(aDouble3, pts));
        final long l4 = System.nanoTime();
        System.out.println("----" + (l4 - l3));
        System.out.println(isInPolygon(aDouble4, pts));
        final long l5 = System.nanoTime();
        System.out.println("----" + (l5 - l4));
        System.out.println(isInPolygon(aDouble5, pts));
        final long l6 = System.nanoTime();
        System.out.println("----" + (l6 - l5));
    }

    /**
     * 判断点是否在多边形内
     *
     * @param point 测试点
     * @param pts   多边形的点
     * @return boolean
     */
    public static boolean isInPolygon(Point2D.Double point, List<Point2D.Double> pts) {

        int N = pts.size();
        // 交叉点数量
        int intersectCount = 0;
        // 浮点类型计算时候与0比较时候的容差
        double precision = 2e-10;
        // 临近顶点
        Point2D.Double p1, p2;

        p1 = pts.get(0);
        for (int i = 1; i <= N; ++i) {
            if (point.equals(p1)) {
                return true;
            }

            p2 = pts.get(i % N);
            if (point.x < Math.min(p1.x, p2.x) || point.x > Math.max(p1.x, p2.x)) {
                p1 = p2;
                continue;
            }

            // 射线穿过算法
            if (point.x > Math.min(p1.x, p2.x) && point.x < Math.max(p1.x, p2.x)) {
                if (point.y <= Math.max(p1.y, p2.y)) {
                    if (p1.x == p2.x && point.y >= Math.min(p1.y, p2.y)) {
                        return true;
                    }

                    if (p1.y == p2.y) {
                        if (p1.y == point.y) {
                            return true;
                        } else {
                            ++intersectCount;
                        }
                    } else {
                        double inters = (point.x - p1.x) * (p2.y - p1.y) / (p2.x - p1.x) + p1.y;
                        if (Math.abs(point.y - inters) < precision) {
                            return true;
                        }

                        if (point.y < inters) {
                            ++intersectCount;
                        }
                    }
                }
            } else {
                if (point.x == p2.x && point.y <= p2.y) {
                    Point2D.Double p3 = pts.get((i + 1) % N);
                    if (point.x >= Math.min(p1.x, p3.x) && point.x <= Math.max(p1.x, p3.x)) {
                        ++intersectCount;
                    } else {
                        intersectCount += 2;
                    }
                }
            }
            p1 = p2;
        }
        //偶数在多边形外
        //奇数在多边形内
        return intersectCount % 2 != 0;
    }
}
