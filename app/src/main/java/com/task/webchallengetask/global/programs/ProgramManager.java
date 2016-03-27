package com.task.webchallengetask.global.programs;

import com.task.webchallengetask.App;
import com.task.webchallengetask.R;
import com.task.webchallengetask.data.database.tables.ProgramTable;
import com.task.webchallengetask.global.Constants;
import com.task.webchallengetask.global.programs.difficults.Difficult;
import com.task.webchallengetask.global.programs.difficults.DifficultActiveBeginner;
import com.task.webchallengetask.global.programs.difficults.DifficultActiveCustom;
import com.task.webchallengetask.global.programs.difficults.DifficultActiveMedium;
import com.task.webchallengetask.global.programs.difficults.DifficultActiveProfessional;
import com.task.webchallengetask.global.programs.difficults.DifficultRunnerBeginner;
import com.task.webchallengetask.global.programs.difficults.DifficultRunnerCustom;
import com.task.webchallengetask.global.programs.difficults.DifficultRunnerMedium;
import com.task.webchallengetask.global.programs.difficults.DifficultRunnerProfessional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by klim on 23.03.16.
 */
public class ProgramManager {

    private static Program createLongDistance() {
        String description = App.getAppContext().getString(R.string.distance_description);

        List<Difficult> difficults = new ArrayList<>();
        difficults.add(new DifficultRunnerBeginner());
        difficults.add(new DifficultRunnerMedium());
        difficults.add(new DifficultRunnerProfessional());
        difficults.add(new DifficultRunnerCustom());

        return new Program("Long distance", description, difficults, Constants.PROGRAM_TYPES.LONG_DISTANCE);
    }

    private static Program createActiveLifeDistance() {
        String description = App.getAppContext().getString(R.string.activity_description);

        List<Difficult> difficults = new ArrayList<>();
        difficults.add(new DifficultActiveBeginner());
        difficults.add(new DifficultActiveMedium());
        difficults.add(new DifficultActiveProfessional());
        difficults.add(new DifficultActiveCustom());

        return new Program("Active life", description, difficults, Constants.PROGRAM_TYPES.ACTIVE_LIFE);
    }

    public static List<Program> getPrograms() {
        List<Program> programs = new ArrayList<>();
        programs.add(ProgramManager.createActiveLifeDistance());
        programs.add(ProgramManager.createLongDistance());
        return programs;
    }

    public static Constants.PROGRAM_TYPES defineProgramType(ProgramTable _table) {
        for (Program program : ProgramManager.getPrograms()) {
            if (program.getName().equals(_table.getName())) {
                return program.getType();
            }
        }
        return null;
    }


}
