package com.ipa989.swshoverworldrngtool.repository.xoroshiro;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SeedSolver {

    static byte[] singleBits(long s0, long s1, int n) {
        byte[] r = new byte[n];
        Xoroshiro random = new Xoroshiro(s0, s1);
        for (int i = 0; i < n; i++) {
            r[i] = (byte) (random.nextInt() & 1);
        }
        return r;
    }

    static BinaryMatrix singleBitsMatrix(int n) {
        byte[][] mat = new byte[128][n];
        for (int i = 0; i < 128; i++) {
            long s0 = 0;
            long s1 = 0;
            if (i < 64) {
                s0 = 1L << i;
            } else {
                s1 = 1L << (i - 64);
            }
            Xoroshiro random = new Xoroshiro(s0, s1);
            for (int j = 0; j < n; j++) {
                mat[i][j] = (byte) (random.nextInt() & 1);
            }
        }
        return BinaryMatrix.getInstance(128, n, mat, false);
    }

    // 現在の内部状態を調べる
    public static List<long[]> solve(byte[] motions) {
        BinaryMatrix f = singleBitsMatrix(motions.length);
        BinaryMatrix g = f.generalizedInverse();
        byte[] base = g.multiplyLeft(motions);
        if (motions.length == 128) {
            BinaryMatrix h = f.multiplyRight(g).add(BinaryMatrix.ones(128));
            byte[][] nullBasis = h.rowBasis();
            int nullRank = nullBasis.length;
            if (nullRank >= 16)
                throw new IllegalStateException("Too less motions. Being not less than 128 recommended.");
            long[] baseLongs = toUnsignedLongArrayBE(base);
            long[][] nullLongBasis = new long[nullRank][];
            for (int i = 0; i < nullLongBasis.length; i++) {
                nullLongBasis[i] = toUnsignedLongArrayBE(nullBasis[i]);
            }
            List<long[]> affine = new ArrayList<>(1 << nullRank);
            for (int i = 0; i < (1 << nullRank); i++) {
                long[] p = Arrays.copyOf(baseLongs, baseLongs.length);
                affine.add(p);
                for (int j = 0; j < nullRank; j++) {
                    int b = (i >>> j) & 1;
                    if (b == 1) {
                        for (int k = 0; k < p.length; k++) {
                            p[k] ^= nullLongBasis[j][k];
                        }
                    }
                }
            }
            return affine;
        } else {
            System.out.println("No");
            return new ArrayList<long[]>(0);
        }
    }

    private static long toUnsignedLongLE(byte[] b) {
        if (b.length != 64) {
            throw new IllegalArgumentException("The lenght of byte array must be 64.");
        }
        long n = 0L;
        for (int i = 0; i < 64; i++) {
            n |= (1L & b[i]) << i;
        }
        return n;
    }

    private static long[] toUnsignedLongArrayBE(byte[] b) {
        if (b.length % 64 != 0) {
            throw new IllegalArgumentException("The lenght of byte array must be a multiple of 64.");
        }
        long[] a = new long[b.length / 64];
        for (int i = 0; i < a.length; i++) {
            byte[] sub = Arrays.copyOfRange(b, 64 * i, 64 * (i + 1));
            a[i] = toUnsignedLongLE(sub);
        }
        return a;
    }

    // 基準シード値が分かっている状態で，現在の内部状態と経過フレーム数を調べる
    public static List<Integer> findMotionStartFrames(byte[] motions, long s0, long s1, int frameStartInclusive,
                                                      int frameEndExclusive) {
        Byte[] motionsBoxed = new Byte[motions.length];
        for(int i=0; i<motions.length; i++) {
            motionsBoxed[i] = motions[i];
        }

        Xoroshiro random = new Xoroshiro(s0, s1);
        // minまで乱数を進める
        for (int i = 0; i < frameStartInclusive; i++) {
            random.nextInt();
        }
        LinkedList<Byte> _motions = new LinkedList<>();
        List<Integer> foundFrames = new ArrayList<>();

        // minからmaxまで検索
        for (int i = frameStartInclusive; i < frameEndExclusive + motionsBoxed.length; i++) {
            if (motionsBoxed.length <= _motions.size()) {
                // 結果が見つかったら
                if (Arrays.equals(motionsBoxed, _motions.toArray(new Byte[motionsBoxed.length]))) {
                    foundFrames.add(i - motionsBoxed.length);
                }
                _motions.removeFirst();
            }
            byte b = (byte) (random.nextInt() & 1);
            _motions.add(b);
        }
        return foundFrames;
    }

    public static byte[] add(byte[] a, byte[] b) {
        if (a.length != b.length) {
            throw new IllegalArgumentException("The lengths of two byte arrays must be same.");
        }
        byte[] c = new byte[a.length];
        for (int i = 0; i < a.length; i++) {
            c[i] = (byte) (a[i] ^ b[i]);
        }
        return c;
    }

    // Mainからの呼び出し
    public static List<String> list(SeedSolverConig config) {
        if (config.s0.isPresent() && config.s1.isPresent()) {
            // 基準シード値が分かっている状態で，現在の内部状態と経過フレーム数を調べる
            List<Integer> motionStartFrame = findMotionStartFrames(config.motions, config.s0.getAsLong(),
                    config.s1.getAsLong(),
                    config.startInclusive, config.endExclusive);

            List<long[]> gameStart = new ArrayList<>();
            for (long count = motionStartFrame.size(); count > 0; count--) {
                long[] longs = new long[]{config.s0.getAsLong(), config.s1.getAsLong()};
                gameStart.add(longs);
            }

            System.out.println("結果　gameStart = "+gameStart + ", motionStartFrame = " + motionStartFrame.size());

            List<String> result = print(gameStart, motionStartFrame, config.motions.length);

            return result;
        }
        else {
            // 現在の内部状態を調べる
            List<long[]> motionStart = solve(config.motions);
            List<Integer> motionStartFrame = Stream.<Integer>generate(() -> 0).limit(motionStart.size()).collect(Collectors.toList());
            List<String> result = print(motionStart, motionStartFrame, config.motions.length);
            return result;

        }
//        return null;
    }

    public static List<String>  print(List<long[]> gameStart, List<Integer> motionStartFrame,
                             int motionLength) {
        System.out.println("結果　print");
        System.out.println("結果　gameStart.size() = " + gameStart.size());
        List<long[]> motionStart = new ArrayList<>(gameStart.size());
        for (int i = 0; i < gameStart.size(); i++) {
            long[] g = gameStart.get(i);
            Xoroshiro random = new Xoroshiro(g[0], g[1]);
            for (int j = 0; j < motionStartFrame.get(i); j++) {
                random.next();
            }
            motionStart.add(random.s);
        }
        List<long[]> motionEnd = new ArrayList<>(motionStart.size());
        for (int i = 0; i < motionStart.size(); i++) {
            long[] s = motionStart.get(i);
            Xoroshiro random = new Xoroshiro(s[0], s[1]);
            for (int j = 0; j < motionLength; j++) {
                random.next();
            }
            motionEnd.add(random.s);
        }
        // 見つかった数
        List<String> entries = new ArrayList<>();
        entries.add(String.format(Locale.US,"%d", gameStart.size()));

        int printLength = 2;
        for (int i = 0; i < gameStart.size() && i < printLength; i++) {
            long[] g = gameStart.get(i);
            long[] s = motionStart.get(i);
            long[] e = motionEnd.get(i);
            int f = motionStartFrame.get(i);

            entries.add(String.format("%016x", e[0])); // s0
            entries.add(String.format("%016x", e[1])); // s1
            entries.add(String.format(Locale.US, "%d", f + motionLength)); // F


            return entries;
        }

        return entries;
    }

}
