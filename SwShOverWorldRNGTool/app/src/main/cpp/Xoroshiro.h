
#ifndef SWSHOVERWORLDRNGTOOL_XOROSHIRO_H
#define SWSHOVERWORLDRNGTOOL_XOROSHIRO_H

#include <iostream>
#include <vector>
using namespace std;

class Xoroshiro {
public:
    unsigned long long state0, state1;

    Xoroshiro(unsigned long long s0, unsigned long long s1)
    {
        this->state0 = s0;
        this->state1 = s1;
    }

    Xoroshiro(unsigned long long s0)
    {
        this->state0 = s0;
        this->state1 = 0x82a2b175229d6a5b;
    }

    // unsigned long long State()
    // {
    //     unsigned long long s0 = this->state0;
    //     unsigned long long s1 = this->state1;
    //     return s0 | (s1 << 64);
    // }

private:
    static unsigned long long RotateLeft(unsigned long long x, int k)
    {
        return (x << k) | (x >> (64 - k));
    }

public:
    unsigned long long Next()
    {
        unsigned long long s0 = state0;
        unsigned long long s1 = state1;

        // stringstream ss0;
        // ss0 << hex << s0;
        // stringstream ss1;
        // ss1 << hex << s1;
        // printf("s0=%s, s1=%s\n", ss0.str(), ss1.str());
        // cout << "s0=" << ss0.str() << ", s1=" << ss1.str() << endl;
        // printf("s0=%X, s1=%X\n", s0, s1);

        unsigned long long result = (s0 + s1);

        s1 ^= s0;
        state0 = (RotateLeft(s0, 24) ^ s1 ^ (s1 << 16));
        state1 = RotateLeft(s1, 37);

        return result;
    }

    unsigned long long Previous()
    {
        unsigned long long s0 = state0;
        unsigned long long s1 = state1;
        s1                    = RotateLeft(s1, 27);
        s0                    = s0 ^ s1 ^ (s1 << 16);
        s0                    = RotateLeft(s0, 40);
        s1 ^= s0;
        state0                    = s0;
        state1                    = s1;
        unsigned long long result = s0 + s1;
        return result;
    }

    unsigned int Nextuint()
    {
        // cout << "nextunsigned int" << endl;
        return (unsigned int)Next();
    }

    unsigned int GetMask(unsigned int x)
    {
        x -= 1;
        x |= x >> 1;
        x |= x >> 2;
        x |= x >> 4;
        x |= x >> 8;
        x |= x >> 16;
        return x;
    }

    // ここでずれてる？
    unsigned long long Rand(unsigned int n = 0xFFFFFFFF)
    {
        unsigned long long mask = GetMask(n);
        unsigned long long res  = Next() & mask;
        // cout << "mask=" << mask << ", res=" << res << endl;
//        stringstream s0, s1;
//        s0 << hex << state0;
//        s1 << hex << state1;
        // cout << "s0=" << s0.str() << ", s1=" << s1.str() << endl;
        while (res >= n) {
            // cout << "res=" << res << endl;
            res = Next() & mask;
        }
        // cout << "mask=" << mask << ", res=" << res << endl;
        return res;
    }
};

#endif //SWSHOVERWORLDRNGTOOL_XOROSHIRO_H
