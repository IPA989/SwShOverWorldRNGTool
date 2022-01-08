
#ifndef SWSHOVERWORLDRNGTOOL_GENERATOR_H
#define SWSHOVERWORLDRNGTOOL_GENERATOR_H

#include <iostream>
#include <sstream>
#include <stdio.h>
#include <string>
#include <vector>
#include <iomanip>
using namespace std;
#include "Xoroshiro.h"

class Generator {

    // Heavily derived from https://github.com/Lincoln-LM/PyNXReader/
public:
    static std::vector<std::string>
    Generate(unsigned long long state0, unsigned long long state1, unsigned long long InitialAdvances,
             unsigned long long advances, unsigned int TSV, unsigned int TRV,
             bool ShinyCharm, bool MarkCharm, bool Weather, bool Static,
             bool Fishing, bool HeldItem, string DesiredMark,
             string DesiredShiny, string DesiredNature, unsigned int LevelMin,
             unsigned int LevelMax, unsigned int SlotMin, unsigned int SlotMax,
             unsigned int MinIVs[], unsigned int MaxIVs[], bool IsAbilityLocked,
             unsigned int EggMoveCount, unsigned int KOs,
             unsigned int FlawlessIVs, bool IsCuteCharm, bool IsShinyLocked,
             bool TIDSIDSearch)
    {

        std::vector<std::string> Results;
        string                   Natures[25] = {
                "Hardy",  "Lonely", "Brave",   "Adamant", "Naughty",
                "Bold",   "Docile", "Relaxed", "Impish",  "Lax",
                "Timid",  "Hasty",  "Serious", "Jolly",   "Naive",
                "Modest", "Mild",   "Quiet",   "Bashful", "Rash",
                "Calm",   "Gentle", "Sassy",   "Careful", "Quirky"};
        int ShinyRolls;
        int MarkRolls;
        if (ShinyCharm)
            ShinyRolls = 3;
        else
            ShinyRolls = 1;
        if (MarkCharm)
            MarkRolls = 3;
        else
            MarkRolls = 1;
        int results = 0;

//        unsigned int TSV = GetTSV(TID, SID);
        int TIDSID = (TSV << 4) + TRV;
        int maxView = 100;
        unsigned int IVs[6];
        bool         GenerateLevel = (LevelMin != LevelMax);
        unsigned int LevelDelta    = LevelMax - LevelMin + 1;

        unsigned int EC, PID, LeadRand = 0, SlotRand = 0, Level = 0,
                BrilliantRand, Nature, AbilityRoll, FixedSeed,
                ShinyXOR, BrilliantThreshold, BrilliantRolls;
        int                BrilliantIVs;
        string             Mark = "", Gender;
        bool               PassIVs, Brilliant, Shiny;
        unsigned long long advance = 0;

        vector<int> br     = GenerateBrilliantInfo(KOs);
        BrilliantThreshold = br[0];
        BrilliantRolls     = br[1];

        Xoroshiro go(state0, state1);

        for (unsigned long long i = 0; i < InitialAdvances; i++) {
            go.Next();
        }

//        while (advance < advances & results < maxView) {
        while (advance < advances) {

            // Init new RNG
            Xoroshiro rng(go.state0, go.state1);
            Brilliant = false;
            Gender    = "";

            if (Static) {
                LeadRand = (unsigned int)rng.Rand(100);
                if (IsCuteCharm && LeadRand <= 65)
                    Gender = "CC";
            }
            else {
                if (!Fishing)
                    rng.Rand();
                rng.Rand(100);
                LeadRand = (unsigned int)rng.Rand(100);
                if (IsCuteCharm && LeadRand <= 65)
                    Gender = "CC";

                // cout << "Slot" << endl;
                SlotRand = (unsigned int)rng.Rand(100);
                if (SlotMin > SlotRand || SlotMax < SlotRand) {
                    if (TIDSIDSearch)
                        go.Previous();
                    else
                        go.Next();
                    advance += 1;
                    continue;
                }

                if (GenerateLevel) {
                    // cout << "GenerateLevel" << endl;
                    Level = LevelMin + (unsigned int)rng.Rand(LevelDelta);
                }
                else {
                    Level = LevelMin;
                }

                Mark =
                        GenerateMark(&rng, Weather, Fishing,
                                     MarkRolls); // Double Mark Gen happens always?
                // cout << "BrilliantRand" << endl;
                BrilliantRand = (unsigned int)rng.Rand(1000);
                if (BrilliantRand < BrilliantThreshold)
                    Brilliant = true;
            }

            Shiny = false;

            unsigned int MockPID = 0;
            if (!IsShinyLocked) {
                int tmp;
                if (Brilliant)
                    tmp = BrilliantRolls;
                else
                    tmp = 0;
                for (int roll = 0; roll < ShinyRolls + tmp; roll++) {
                    // cout << "mockPID" << endl;
                    MockPID = rng.Nextuint();
                    Shiny = (((MockPID >> 16) ^ (MockPID & 0xFFFF)) ^ TIDSID) < 16;
                    if (Shiny)
                        break;
                }
            }

            if (Gender != "CC")
                if (rng.Rand(2) == 0)
                    Gender = "F";
                else
                    Gender = "M";
            Nature      = (unsigned int)rng.Rand(25);
            AbilityRoll = 2;
            if (!IsAbilityLocked)
                AbilityRoll = (unsigned int)rng.Rand(2);

            if (!Static && HeldItem)
                rng.Rand(100);

            BrilliantIVs = 0;
            if (Brilliant) {
                BrilliantIVs = (int)rng.Rand(2) | 2;
                if (EggMoveCount > 1)
                    rng.Rand(EggMoveCount);
            }

            FixedSeed = rng.Nextuint();


            vector<string> strings = CalculateFixed(
                    FixedSeed, TIDSID, Shiny,
                    (int)(FlawlessIVs + BrilliantIVs), MinIVs, MaxIVs);
            // strings{EC, PID, IVs[6], ShinyXOR, PassIVs}

            if (TIDSIDSearch) {
                ShinyXOR = 0;
            }
            if (strings[4] == "True")
                PassIVs = true;
            else
                PassIVs = false;

            string shinyxor;
            if (ShinyXOR == 0)
                shinyxor = "Square";
            else if (ShinyXOR < 16)
                shinyxor = "Star";
            else
                shinyxor = "No";

            if (!PassIVs || (DesiredShiny == "Square" && ShinyXOR != 0) ||
                (DesiredShiny == "Star" && (ShinyXOR > 15 || ShinyXOR == 0)) ||
                (DesiredShiny == "Star/Square" && ShinyXOR > 15) ||
                (DesiredShiny == "No" && ShinyXOR < 16)) {
                if (TIDSIDSearch)
                    go.Previous();
                else
                    go.Next();
                advance += 1;
                continue;
            }

            Mark = GenerateMark(&rng, Weather, Fishing, MarkRolls);

            if (!PassesMarkFilter(Mark, DesiredMark)) {
                if (TIDSIDSearch)
                    go.Previous();
                else
                    go.Next();
                advance += 1;
                continue;
            }

            if (!PassesNatureFilter(Natures[Nature], DesiredNature)) {
                if (TIDSIDSearch)
                    go.Previous();
                else
                    go.Next();
                advance += 1;
                continue;
            }

            int Animation = Xoroshiro(go.state0, go.state1).Next() % 2;
            std::ostringstream sout;
            // Passes all filters!
            sout << std::setfill(' ') << std::setw(6) << advance + InitialAdvances;
            Results.push_back(sout.str()); // Frame
            sout.str("");
            sout.clear();
            sout << std::setfill('0') << std::setw(4) << (((MockPID >> 16) ^ (MockPID & 0xFFFF)) >> 4);
            Results.push_back(sout.str()); // TSV
            sout.str("");
            sout.clear();
            sout << std::setfill(' ') << std::setw(7) << shinyxor;
            Results.push_back(shinyxor);
            sout.str("");
            sout.clear();
            sout << std::setfill(' ') << std::setw(12) << Mark;
            Results.push_back(sout.str());
            sout.str("");
            sout.clear();
            Results.push_back(strings[2]); // IVs
            if (AbilityRoll == 0)
                Results.push_back("1");
            else
                Results.push_back("0");
            Results.push_back(Gender);
            sout << std::setfill(' ') << std::setw(7) << Natures[Nature];
            Results.push_back(sout.str());
            sout.str("");
            sout.clear();
            sout << std::setfill(' ') << std::setw(2) << Level;
            Results.push_back(sout.str());
            sout.str("");
            sout.clear();
            sout << std::setfill(' ') << std::setw(2) << SlotRand;
            Results.push_back(sout.str());
            sout.str("");
            sout.clear();
            sout << std::setfill('0') << std::setw(8) << strings[1];
            Results.push_back(sout.str()); // PID
            sout.str("");
            sout.clear();
            sout << std::setfill('0') << std::setw(8) << strings[0];
            Results.push_back(sout.str()); // EC
            sout.str("");
            sout.clear();
            if (Brilliant)
                Results.push_back("Y");
            else
                Results.push_back("-");
            stringstream s0;
            s0 << hex << go.state0;
            sout.str("");
            sout.clear();
            sout << std::setfill('0') << std::setw(16) << s0.str();
            Results.push_back(sout.str());
            stringstream s1;
            s1 << hex << go.state1;
            sout.str("");
            sout.clear();
            sout << std::setfill('0') << std::setw(16) << s1.str();
            Results.push_back(sout.str());
            string animation = std::to_string(Animation);
            Results.push_back(animation);

            if (TIDSIDSearch)
                go.Previous();
            else
                go.Next();
            advance += 1;
            results++;
        }

        return Results;
    }

    static unsigned int GetTSV(unsigned int TID, unsigned int SID)
    {
        return TID ^ SID;
    }

    static string GenerateMark(Xoroshiro *go, bool Weather, bool Fishing,
                               int MarkRolls)
    {
        string PersonalityMarks[28] = {
                "Rowdy",     "AbsentMinded", "Jittery",  "Excited", "Charismatic",
                "Calmness",  "Intense",      "ZonedOut", "Joyful",  "Angry",
                "Smiley",    "Teary",        "Upbeat",   "Peeved",  "Intellectual",
                "Ferocious", "Crafty",       "Scowling", "Kindly",  "Flustered",
                "PumpedUp",  "ZeroEnergy",   "Prideful", "Unsure",  "Humble",
                "Thorny",    "Vigor",        "Slump"};
        for (int i = 0; i < MarkRolls; i++) {
            // cout << "start mark" << endl;
            unsigned int rare = (unsigned int)go->Rand(1000);
            unsigned int pers = (unsigned int)go->Rand(100);
            unsigned int unco = (unsigned int)go->Rand(50);
            unsigned int weat = (unsigned int)go->Rand(50);
            unsigned int time = (unsigned int)go->Rand(50);
            unsigned int fish = (unsigned int)go->Rand(25);

            if (rare == 0)
                return "Rare";
            if (pers == 0) {
                // cout << "personal mark" << endl;
                return PersonalityMarks[go->Rand(28)];
            }
            if (unco == 0)
                return "Uncommon";
            if (weat == 0 && Weather)
                return "Weather";
            if (time == 0)
                return "Time";
            if (fish == 0 && Fishing)
                return "Fish";
            // cout << "end mark" << endl;
        }
        return "None";
    }

private:
    static unsigned int FixedEC, FixedPID, FixedIVIndex, i;

    static bool PassIVs;

public:
    static std::vector<string>
    CalculateFixed(unsigned int FixedSeed, unsigned int TSV, bool Shiny,
                   int ForcedIVs, unsigned int MinIVs[], unsigned int MaxIVs[])
    {
        unsigned int FixedEC, FixedPID, FixedIVIndex, i;

        bool PassIVs;
        // unsigned long tmp = 0x82A2B175229D6A5B;
        Xoroshiro go(FixedSeed, (unsigned long long)0x82a2b175229d6a5b);

        FixedEC  = go.Nextuint();
        FixedPID = go.Nextuint();

        if (!Shiny) {
            if (((FixedPID >> 16) ^ (FixedPID & 0xFFFF) ^ TSV) < 16)
                FixedPID ^= 0x10000000; // Antishiny
        }
        else {
            if (!(((FixedPID >> 16) ^ (FixedPID & 0xFFFF) ^ TSV) < 16))
                FixedPID = (((TSV ^ (FixedPID & 0xFFFF)) << 16) |
                            (FixedPID & 0xFFFF)) &
                           0xFFFFFFFF;
        }

        unsigned int IVs[] = {32, 32, 32, 32, 32, 32};
        PassIVs            = true;
        for (i = 0; i < ForcedIVs; i++) {
            FixedIVIndex = (unsigned int)go.Rand(6);
            while (IVs[FixedIVIndex] != 32) {
                FixedIVIndex = (unsigned int)go.Rand(6);
            }
            IVs[FixedIVIndex] = 31;
            if (IVs[FixedIVIndex] > MaxIVs[FixedIVIndex]) {
                PassIVs = false;
                break;
            }
        }

        for (i = 0; i < 6 && PassIVs; i++) {
            if (IVs[i] == 32)
                IVs[i] = (unsigned int)go.Rand(32);
            if (IVs[i] < MinIVs[i] || IVs[i] > MaxIVs[i]) {
                PassIVs = false;
                break;
            }
        }

        string iv;
        std::ostringstream sout;
        for (i = 0; i < 6; i++) {
            sout << std::setfill(' ') << std::setw(2) << IVs[i];
            iv += sout.str();
            sout.str("");
            sout.clear();
            if(i!=5){
                iv += "-";
            }
        }

        vector<string> result;

        long         ShinyXOR = GetTSV(FixedPID >> 16, FixedPID & 0xFFFF) ^ TSV;
        stringstream ec;
        ec << hex << FixedEC;
        result.push_back(ec.str());
        stringstream pid;
        pid << hex << FixedPID;
        result.push_back(pid.str());
        result.push_back(iv);
        string shiny = std::to_string(ShinyXOR);
        result.push_back(shiny);
        if (PassIVs == true)
            result.push_back("True");
        else
            result.push_back("False");
        string pass;
        if (PassIVs)
            pass = "True";
        else
            pass = "False";
        result.push_back(pass);
        return result;
    }

private:
    static bool PassesMarkFilter(string Mark, string DesiredMark)
    {
        return !((DesiredMark == "Any Mark" && Mark == "None") ||
                 (DesiredMark == "Any Personality" &&
                  (Mark == "None" || Mark == "Uncommon" || Mark == "Time" ||
                   Mark == "Weather" || Mark == "Fishing" || Mark == "Rare")) ||
                 (DesiredMark != "Ignore" && DesiredMark != "Any Mark" &&
                  DesiredMark != "Any Personality" && Mark != DesiredMark));
    }

    static bool PassesNatureFilter(string Nature, string DesiredNature)
    {
        return ((DesiredNature == Nature) || (DesiredNature == "Ignore"));
    }

    static std::vector<int> GenerateBrilliantInfo(int KOs)
    {
        vector<int> result = {0, 0};
        if (KOs >= 500) {
            result[0] = 30;
            result[1] = 6;
        }
        if (KOs >= 300) {
            result[0] = 30;
            result[1] = 5;
        }
        if (KOs >= 200) {
            result[0] = 30;
            result[1] = 4;
        }
        if (KOs >= 100) {
            result[0] = 30;
            result[1] = 3;
        }
        if (KOs >= 50) {
            result[0] = 25;
            result[1] = 2;
        }
        if (KOs >= 20) {
            result[0] = 20;
            result[1] = 1;
        }
        if (KOs >= 1) {
            result[0] = 15;
            result[1] = 1;
        }

        return result;
    }

public:
    static string GenerateRetailSequence(unsigned long long state0,
                                         unsigned long long state1,
                                         unsigned int start, unsigned int max)
    {
        Xoroshiro go(state0, state1);
        for (int i = 0; i < start; i++)
            go.Next();

        string ret;

        for (unsigned int i = 0; i < max; i++) {

            ret += go.Next() & 1;
        }

        return ret;
    }
};


#endif //SWSHOVERWORLDRNGTOOL_GENERATOR_H
