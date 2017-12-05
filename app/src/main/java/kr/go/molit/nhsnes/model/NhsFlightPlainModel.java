package kr.go.molit.nhsnes.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by jongrakmoon on 2017. 5. 25..
 */
@RealmClass
public class NhsFlightPlainModel implements RealmModel,Serializable {

    public enum FindType {
        ALL, APPROVED, DENIED, TMP
    }


    public static final int FLIGT_STATUS_NONE = 0;
    public static final int FLIGT_STATUS_APPROVED = 1;
    public static final int FLIGT_STATUS_DENIED = 2;

    @PrimaryKey
    private String flightIdentity;
    private String flightRules;
    private int flightType;
    private String number;
    private String aircraftType;
    private String wakeTurbCat;
    private String equipment;
    private String departureAerodrome;
    private String time;
    private String crusingSpeed;
    private String flightLevel;
    private int propose;
    private String route;
    private String arrivalAerodrome;
    private String totalEEF;
    private String firstANTN;
    private String secondALTN;
    private String otherInfo;
    private boolean isSuperLightPlane;
    private String e;
    private String p;

    //int 0:UHF, 1:VHF, 2:ELT
    private int r;
    //int 0:Polar, 1:Desert, 2:Maritime, 3:Jungle
    private int s;
    //int 0:Light, 1:Fluroes, 2:UHF, 3:VHF
    private int j;


    private String capacity;
    private String numbers;
    private String cover;
    private String color;

    private String a;
    private String n;
    private String c;

    private boolean isLike;

    private long regDate;

    private boolean isChecked;

    private int flightStatus;


    public String getFlightIdentity() {
        return flightIdentity;
    }

    public void setFlightIdentity(String flightIdentity) {
        this.flightIdentity = flightIdentity;
    }

    public String getFlightRules() {
        return flightRules;
    }

    public void setFlightRules(String flightRules) {
        this.flightRules = flightRules;
    }


    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAircraftType() {
        return aircraftType;
    }

    public void setAircraftType(String aircraftType) {
        this.aircraftType = aircraftType;
    }

    public String getWakeTurbCat() {
        return wakeTurbCat;
    }

    public void setWakeTurbCat(String wakeTurbCat) {
        this.wakeTurbCat = wakeTurbCat;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getDepartureAerodrome() {
        return departureAerodrome;
    }

    public void setDepartureAerodrome(String departureAerodrome) {
        this.departureAerodrome = departureAerodrome;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCrusingSpeed() {
        return crusingSpeed;
    }

    public void setCrusingSpeed(String crusingSpeed) {
        this.crusingSpeed = crusingSpeed;
    }

    public String getFlightLevel() {
        return flightLevel;
    }

    public void setFlightLevel(String flightLevel) {
        this.flightLevel = flightLevel;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getArrivalAerodrome() {
        return arrivalAerodrome;
    }

    public void setArrivalAerodrome(String arrivalAerodrome) {
        this.arrivalAerodrome = arrivalAerodrome;
    }

    public String getTotalEEF() {
        return totalEEF;
    }

    public void setTotalEEF(String totalEEF) {
        this.totalEEF = totalEEF;
    }

    public String getFirstANTN() {
        return firstANTN;
    }

    public void setFirstANTN(String firstANTN) {
        this.firstANTN = firstANTN;
    }

    public String getSecondALTN() {
        return secondALTN;
    }

    public void setSecondALTN(String secondALTN) {
        this.secondALTN = secondALTN;
    }

    public String getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }

    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }


    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public long getRegDate() {
        return regDate;
    }

    public void setRegDate(long regDate) {
        this.regDate = regDate;
    }

    public int isR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public int isS() {
        return s;
    }

    public void setS(int s) {
        this.s = s;
    }

    public int isJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public NhsFlightPlainModel() {
        this.regDate = System.currentTimeMillis();
        isChecked = false;
    }

    public int getFlightStatus() {
        return flightStatus;
    }

    public void setFlightStatus(int flightStatus) {
        this.flightStatus = flightStatus;
    }

    public boolean save() {
        boolean result = false;
        Realm realm = Realm.getDefaultInstance();

        if (realm.where(NhsFlightPlainModel.class).equalTo("flightIdentity", this.flightIdentity).findFirst() != null) {
            return false;
        } else {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealm(NhsFlightPlainModel.this);
                }
            });
            return true;
        }
    }

    public boolean update() {
        Realm realm = Realm.getDefaultInstance();

        if (realm.where(NhsFlightPlainModel.class).equalTo("flightIdentity", this.flightIdentity).findFirst() == null) {
            return false;
        } else {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealmOrUpdate(NhsFlightPlainModel.this);
                }
            });
            return true;
        }
    }

    public void delete() {
        Realm realm = Realm.getDefaultInstance();
        final RealmResults<NhsFlightPlainModel> models = realm.where(NhsFlightPlainModel.class)
                .equalTo("flightIdentity", flightIdentity)
                .findAll();

        System.out.println(models.toString());
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                models.deleteAllFromRealm();
            }
        });
    }

    public static NhsFlightPlainModel getOneById(String flightIdentity) {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(NhsFlightPlainModel.class).equalTo("flightIdentity", flightIdentity).findFirst();

    }

    public static List<NhsFlightPlainModel> find(String departureAerodrome, FindType type) {
        Realm realm = Realm.getDefaultInstance();
        RealmQuery<NhsFlightPlainModel> query = realm.where(NhsFlightPlainModel.class).like("departureAerodrome", "*" + departureAerodrome + "*");
        if (type == FindType.ALL) {

        } else if (type == FindType.APPROVED) {
            query = query.equalTo("flightStatus", FLIGT_STATUS_APPROVED);
        } else if (type == FindType.DENIED) {
            query = query.equalTo("flightStatus", FLIGT_STATUS_DENIED);
        } else if (type == FindType.TMP) {

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            long today;
            try {
                today = format.parse(format.format(System.currentTimeMillis())).getTime();
            } catch (ParseException e1) {
                today = System.currentTimeMillis();
            }
            query = query.between("regDate", today, today + (24 * 60 * 60 * 1000));
        }

        return query.findAll();

    }

    public static List<NhsFlightPlainModel> findAll() {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(NhsFlightPlainModel.class).findAll();
    }


    public boolean getSuperLightPlane() {
        return isSuperLightPlane;
    }

    public void setSuperLightPlane(boolean superLightPlane) {
        isSuperLightPlane = superLightPlane;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getFlightType() {
        return flightType;
    }

    public void setFlightType(int flightType) {
        this.flightType = flightType;
    }

    public int getPropose() {
        return propose;
    }

    public void setPropose(int propose) {
        this.propose = propose;
    }
}
