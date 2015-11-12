package oodesignanalysis.hiketracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by diana on 10/22/15.
 */
public class MountainDataSource extends HikeTrackerDBDAO{

    private static final String WHERE_ID_EQUALS = DatabaseHelper.KEY_ID
            + " =?";

    public MountainDataSource(Context context) {
        super(context);
    }

    public long save(Mountain mountain) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_PEAKNAME, mountain.getmName());

        return database.insert(DatabaseHelper.TABLE_MOUNTAIN, null, values);
    }

    public long update(Mountain mountain) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_PEAKNAME, mountain.getmName());

        long result = database.update(DatabaseHelper.TABLE_MOUNTAIN, values,
                WHERE_ID_EQUALS,
                new String[] { String.valueOf(mountain.getId()) });
        Log.d("Update Result:", "=" + result);
        return result;

    }

    public int deleteMountain(Mountain mountain) {
        return database.delete(DatabaseHelper.TABLE_MOUNTAIN,
                WHERE_ID_EQUALS, new String[] { mountain.getmName() + "" });
    }

    public List<Mountain> getMountains() {
        List<Mountain> mountains = new ArrayList<Mountain>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_MOUNTAIN,
                new String[] { DatabaseHelper.KEY_ID,
                        DatabaseHelper.KEY_PEAKNAME }, null, null, null, null,
                null);

        while (cursor.moveToNext()) {
            Mountain mountain = new Mountain();
            mountain.setId(cursor.getInt(0));
            mountain.setmName(cursor.getString(1));
            mountains.add(mountain);
        }
        return mountains;
    }

    // Method to load mountains if not already in database
    public void loadMountains() {
        List<Mountain> mountains = addMountains();

        // insert into the database
        for (Mountain mount: mountains){
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.KEY_PEAKNAME, mount.getmName());
            values.put(DatabaseHelper.KEY_RANGE, mount.getmRange());
            values.put(DatabaseHelper.KEY_ELEVATION, mount.getmElevation());
            values.put(DatabaseHelper.KEY_LATITUDE, mount.getmLatitude());
            values.put(DatabaseHelper.KEY_LONGITUDE, mount.getmLongtidue());
            database.insert(DatabaseHelper.TABLE_MOUNTAIN, null, values);
        }
    }

    private List<Mountain> addMountains() {
        List<Mountain> mountains = new ArrayList<>();
        mountains.add(new Mountain("Grays Peak", "Front Range", 14270, 39.633820, 105.817520));
        mountains.add(new Mountain("Torreys Peak", "Front Range",14270,  39.633820,105.817520 ));
        mountains.add(new Mountain("Mt. Evans", "Front Range",14264, 39.588360,105.643333 ));
        mountains.add(new Mountain("Longs Peak", "Front Range",14255,40.254902, 105.615738 ));
        mountains.add(new Mountain("Pikes Peak", "Front Range",14110, 38.840542,105.044357 ));
        mountains.add(new Mountain("Mt. Bierstadt", "Front Range",14060,39.582638, 105.668610 ));
        mountains.add(new Mountain("Quandary Peak", "Tenmile Range", 14265,39.397236,106.106430 ));
        mountains.add(new Mountain("Mt. Lincoln", "Mosquito Range",14286, 39.351391,106.111404 ));
        mountains.add(new Mountain("Mt. Cameron", "Mosquito Range", 14238,39.346844,106.118576 ));
        mountains.add(new Mountain("Mt. Bross", "Mosquito Range", 14172,39.335060, 106.107376));
        mountains.add(new Mountain("Mt. Democrat", "Mosquito Range", 14148, 39.339542, 106.139946));
        mountains.add(new Mountain("Mt. Sherman", "Mosquito Range", 14036, 39.225006, 106.169945));
        mountains.add(new Mountain("Mt. Elbert", "Sawatch Range", 14433, 39.118075, 106.445417));
        mountains.add(new Mountain("Mt. Massive", "Sawatch Range",14421, 39.187298, 106.475548 ));
        mountains.add(new Mountain("Mt. Harvard", "Sawatch Range",14420, 38.924328, 106.320618));
        mountains.add(new Mountain("La Plata Peak", "Sawatch Range", 14336,39.029251,106.473145 ));
        mountains.add(new Mountain("Mt. Antero", "Sawatch Range", 14269, 38.674088, 106.246201));
        mountains.add(new Mountain("Mt. Shavano", "Sawatch Range", 14229, 38.619083, 106.239296));
        mountains.add(new Mountain("Mt. Princeton", "Sawatch Range",14197, 38.749062, 106.242432));
        mountains.add(new Mountain("Mt. Belford", "Sawatch Range", 14197, 38.960575, 106.360832));
        mountains.add(new Mountain("Mt. Yale", "Sawatch Range",14196, 38.844051, 106.313965));
        mountains.add(new Mountain("Tabeguache Peak", "Sawatch Range",14155, 38.625622, 106.250710));
        mountains.add(new Mountain("Mt. Oxford", "Sawatch Range",14153, 38.964680, 106.338432));
        mountains.add(new Mountain("Mt. Columbia", "Sawatch Range",14073, 38.903957, 106.297485 ));
        mountains.add(new Mountain("Missouri Mountain", "Sawatch Range", 14067, 38.947559, 106.378471));
        mountains.add(new Mountain("Mt. of the Holy Cross", "Sawatch Range",14005, 39.466713, 106.481766 );
        mountains.add(new Mountain("Huron Peak", "Sawatch Range", 14003, 38.945423, 106.438126));
        mountains.add(new Mountain("Castle Peak", "Elk Mountains",14265, 39.009647, 106.86144));
        mountains.add(new Mountain("Maroon Peak", "Elk Mountains", ));
        mountains.add(new Mountain("Capitol Peak", "Elk Mountains", ));00
        mountains.add(new Mountain("Snowmass Mountain", "Elk Mountains", ));
        mountains.add(new Mountain("Conundrum Peak", "Elk Mountains", ));
        mountains.add(new Mountain("Pyramid Peak", "Elk Mountains", ));
        mountains.add(new Mountain("North Maroon Peak", "Elk Mountains", ));
        mountains.add(new Mountain("Uncompahgre Peak", "San Jaun Mountains", ));
        mountains.add(new Mountain("Mt. Wilson", "San Jaun Mountains", ));
        mountains.add(new Mountain("El Diente Peak", "San Jaun Mountains", ));
        mountains.add(new Mountain("Mt. Sneffels", "San Jaun Mountains", ));
        mountains.add(new Mountain("Mt. Eolus", "San Jaun Mountains", ));
        mountains.add(new Mountain("Windom Peak", "San Jaun Mountains", ));
        mountains.add(new Mountain("Sunlight Peak", "San Jaun Mountains", ));
        mountains.add(new Mountain("Handies Peak", "San Jaun Mountains", ));
        mountains.add(new Mountain("North Eolus", "San Jaun Mountains", ));
        mountains.add(new Mountain("Redcloud Peak", "San Jaun Mountains", ));
        mountains.add(new Mountain("Wilson Peak", "San Jaun Mountains", ));
        mountains.add(new Mountain("Wetterhorn Peak", "San Jaun Mountains", ));
        mountains.add(new Mountain("San Luis Peak", "San Jaun Mountains", ));
        mountains.add(new Mountain("Sunshine Peak", "San Jaun Mountains", ));
        mountains.add(new Mountain("Blanca Peak", "Sangre de Cristo Range", ));
        mountains.add(new Mountain("Crestone Peak", "Sangre de Cristo Range", ));
        mountains.add(new Mountain("Crestone Needle", "Sangre de Cristo Range", ));
        mountains.add(new Mountain("Kit Carson Peak", "Sangre de Cristo Range", ));
        mountains.add(new Mountain("Challenger Point", "Sangre de Cristo Range", ));
        mountains.add(new Mountain("Humboldt Peak", "Sangre de Cristo Range", ));
        mountains.add(new Mountain("Culebra Peak", "Sangre de Cristo Range", ));
        mountains.add(new Mountain("Ellingwood Point", "Sangre de Cristo Range", ));
        mountains.add(new Mountain("Mt. Lindsey", "Sangre de Cristo Range", 14042, 37.583801,105.444763 ));
        mountains.add(new Mountain("Little Bear Peak", "Sangre de Cristo Range", ));
        return mountains;
    }

}
