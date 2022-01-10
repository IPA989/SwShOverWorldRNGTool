#include <jni.h>
#include <string>
#include "Xoroshiro.h"
#include "Generator.h"
#include <vector>

// 出力を100件区切りにする
// 結果最大数設定
// returnをjobjectarrayでvector<string>で行区切りで出力すれば色違いの色を変えられる?

extern "C" JNIEXPORT jstring JNICALL
Java_com_ipa989_swshoverworldrngtool_MainActivity_resultFromJNI(
        JNIEnv* env,
        jobject ,
        jstring state0, jstring state1,
        jlong AdvMin, jlong AdvMax, jint TSV, jint TRV,
        jboolean ShinyCharm, jboolean MarkCharm, jboolean Weather, jboolean Static,
        jboolean Fishing, jboolean HeldItem, jstring DesiredMark,
        jstring DesiredShiny, jstring DesiredNature, jint LevelMin,
        jint LevelMax, jint SlotMin, jint SlotMax,
        jintArray MinIVs, jintArray MaxIVs, jboolean IsAbilityLocked,
        jint EggMoveCount, jint KOs,
        jint FlawlessIVs, jboolean IsCuteCharm, jboolean IsShinyLocked,
        jboolean TSVSearch)
{
    std::string resultLabel[] = {"[F]", "TSV", "   Shiny", "       Mark", "IVs(HABCDS)", "Ability", "Gender", "Nature", "Lv", "Slot", "PID", "EC", "Brilliant", "  s0", "  s1", "Motion"};
    bool output[]             = {true, false, true, true, true, true, true, true, true, true, true, true, false, true, true, true};

    const char *cstr0 = env->GetStringUTFChars(state0, NULL);
    std::string s0 = std::string(cstr0, 16);
    const char *cstr1 = env->GetStringUTFChars(state1, NULL);
    std::string s1 = std::string(cstr1, 16);

    unsigned long long st0 = stoll(s0, nullptr, 16);
    unsigned long long st1 = stoll(s1, nullptr, 16);

    const char *Mark = env->GetStringUTFChars(DesiredMark, nullptr);
    std::string desiredMark = std::string(Mark);
    const char *Shiny = env->GetStringUTFChars(DesiredShiny, nullptr);
    std::string desiredShiny = std::string(Shiny);
    const char *Nature = env->GetStringUTFChars(DesiredNature, nullptr);
    std::string desiredNature = std::string(Nature);

    jint *array = env->GetIntArrayElements(MinIVs, nullptr);
    unsigned int minIVs[6];
    for (int i=0; i<6; i++) {
        minIVs[i] = array[i];
    }
    env->ReleaseIntArrayElements(MinIVs, array, NULL);

    array = env->GetIntArrayElements(MaxIVs, nullptr);
    unsigned int maxIVs[6];
    for (int i=0; i<6; i++) {
        maxIVs[i] = array[i];
    }
    env->ReleaseIntArrayElements(MinIVs, array, NULL);


    std::vector<string> results =
            Generator::Generate(st0, st1, AdvMin, AdvMax, TSV, TRV, ShinyCharm, MarkCharm, Weather,
                                Static, Fishing, HeldItem, desiredMark, desiredShiny,
                                desiredNature, LevelMin, LevelMax, SlotMin, SlotMax, minIVs,
                                maxIVs, IsAbilityLocked, EggMoveCount, KOs, FlawlessIVs,
                                IsCuteCharm, IsShinyLocked, TSVSearch);

    std::string Result;

    int n = results.size();
    Result += std::to_string(n/16);
    Result += " results: \n";

    for (int i = 0; i < 16; i++) {
        int r = i%16;
        if(output[r]) {
            Result += resultLabel[r];
            Result += ", ";
        }
    }

    if (n > 0) {
        for (int i = 0; i < results.size(); i++) {
            int r = i % 16;
            if (r == 0) {
                Result += "\n";
            }
            if(output[r]){
                Result += results[i];
                if(r!=15)
                Result += ", ";
            }
        }
    }

    return env->NewStringUTF(Result.c_str());
}

//extern "C"
//JNIEXPORT jstring JNICALL
//Java_com_example_swshoverworldrngtool_MainActivity_stringFromJNI(JNIEnv* env,
//                                                                 jobject/* this */) {
//    string result = "result from c++";
//    return env->NewStringUTF(result.c_str());
//}