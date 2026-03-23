package com.project.realhealthbuddy.Steps.Repository;




import android.content.Context;
import com.project.realhealthbuddy.Steps.Data.StepsEntity;
import com.project.realhealthbuddy.Steps.Repository.StepsRepository;
import com.project.realhealthbuddy.Steps.Data.MedicineDataManager;
import com.project.realhealthbuddy.Model.Medicine;
import java.util.List;

public class HealthRepository {
    private StepsRepository stepsRepository;

    public HealthRepository(Context context) {
        stepsRepository = new StepsRepository(context);
    }

    public List<StepsEntity> getLast7Days() {
        return stepsRepository.getLast7Days();  // Your existing method
    }

    public StepsEntity getTodaySteps(String date) {
        return stepsRepository.getStepsByDate(date);  // Your existing method
    }

    public List<Medicine> getTodayMedicines(Context context) {
        return MedicineDataManager.getTodayMedicines(context);  // MedicineFragment SharedPrefs
    }
}