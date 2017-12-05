package kr.go.molit.nhsnes.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.annotations.PrimaryKey;

import static kr.go.molit.nhsnes.model.NhsFlightPlainModel.FLIGT_STATUS_APPROVED;
import static kr.go.molit.nhsnes.model.NhsFlightPlainModel.FLIGT_STATUS_DENIED;

/**
* 비행이력조회 모델
* @author FIESTA
* @since  오전 4:43
**/
public class NhsFlightHistoryModel extends RealmObject implements Serializable {

    public enum FindType {
        ALL, APPROVED, DENIED, TMP
    }

    @PrimaryKey
    private long idx;
    private String callsign;
    private String planId;
    private String planSn;
    private String acrftCd;
    private String planStatus;
    private String planDoccd;
    private String messageType;
    private String planPurpose;
    private String planMbrId;
    private String planDate;
    private String planArvDate;
    private String planRfsDate;
    private String planPriority;
    private String planFltm;
    private String planRqstdept;
    private String flightRule;
    private String flightType;
    private String planNumber;
    private String acrftType;
    private String wakeTurbcat;
    private String planEquipment;
    private String planDeparture;
    private String planEtd;
    private String planAtd;
    private String cruisingSpeed;
    private String flightLevel;
    private String planRoute;
    private String planArrival;
    private String oneAltn;
    private String twoAltn;
    private String planTeet;
    private String planEta;
    private String planAta;
    private String otherInfo;
    private String flightPsbtime;
    private String flightPerson;
    private String rrUhf;
    private String rrVhf;
    private String rrElt;
    private String emgcPolar;
    private String emgcDesert;
    private String emgcMaritime;
    private String emgcJungle;
    private String lifejkLight;
    private String lifejkFluores;
    private String lifejkUhf;
    private String lifejkVhf;
    private String lifebtNumber;
    private String lifebtPerson;
    private String lifebtCover;
    private String lifebtColor;
    private String acrftColor;
    private String captainPhone;
    private String planPresent;
    private String departureAerodrome;
    private Integer saveType;
    private long regDate;
    private long gpsLogDate;
    private String flightId;

    private String startDate;   // 시작 일자
    private long totalDistanc;  //  총 비행 거리
    private int avgSpeed;       // 평균 속도
    private int avgAltitude;  // 평균 고도
    private String startTime;   // 시작 시간
    private String endTime;     // 도착 시간
    private long totalFlightTime;   // 총 비행 시간(밀리세컨드)

    public long getTotalFlightTime() {
        return totalFlightTime;
    }

    public void setTotalFlightTime(long totalFlightTime) {
        this.totalFlightTime = totalFlightTime;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public long getTotalDistanc() {
        return totalDistanc;
    }

    public void setTotalDistanc(long totalDistanc) {
        this.totalDistanc = totalDistanc;
    }

    public int getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(int avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public int getAvgAltitude() {
        return avgAltitude;
    }

    public void setAvgAltitude(int avgAltitude) {
        this.avgAltitude = avgAltitude;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }

    public long getGpsLogDate() {
        return gpsLogDate;
    }

    public void setGpsLogDate(long gpsLogDate) {
        this.gpsLogDate = gpsLogDate;
    }

    public String getDepartureAerodrome() {
        return departureAerodrome;
    }

    public void setDepartureAerodrome(String departureAerodrome) {
        this.departureAerodrome = departureAerodrome;
    }

    public long getRegDate() {
        return regDate;
    }

    public void setRegDate(long regDate) {
        this.regDate = regDate;
    }

    public long getIdx() {
        return idx;
    }

    public void setIdx(long idx) {
        this.idx = idx;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getPlanSn() {
        return planSn;
    }

    public void setPlanSn(String planSn) {
        this.planSn = planSn;
    }

    public String getAcrftCd() {
        return acrftCd;
    }

    public void setAcrftCd(String acrftCd) {
        this.acrftCd = acrftCd;
    }

    public String getPlanStatus() {
        return planStatus;
    }

    public void setPlanStatus(String planStatus) {
        this.planStatus = planStatus;
    }

    public String getPlanDoccd() {
        return planDoccd;
    }

    public void setPlanDoccd(String planDoccd) {
        this.planDoccd = planDoccd;
    }

    public String getCallsign() {
        return callsign;
    }

    public void setCallsign(String callsign) {
        this.callsign = callsign;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getPlanPurpose() {
        return planPurpose;
    }

    public void setPlanPurpose(String planPurpose) {
        this.planPurpose = planPurpose;
    }

    public String getPlanMbrId() {
        return planMbrId;
    }

    public void setPlanMbrId(String planMbrId) {
        this.planMbrId = planMbrId;
    }

    public String getPlanDate() {
        return planDate;
    }

    public void setPlanDate(String planDate) {
        this.planDate = planDate;
    }

    public String getPlanArvDate() {
        return planArvDate;
    }

    public void setPlanArvDate(String planArvDate) {
        this.planArvDate = planArvDate;
    }

    public String getPlanRfsDate() {
        return planRfsDate;
    }

    public void setPlanRfsDate(String planRfsDate) {
        this.planRfsDate = planRfsDate;
    }

    public String getPlanPriority() {
        return planPriority;
    }

    public void setPlanPriority(String planPriority) {
        this.planPriority = planPriority;
    }

    public String getPlanFltm() {
        return planFltm;
    }

    public void setPlanFltm(String planFltm) {
        this.planFltm = planFltm;
    }

    public String getPlanRqstdept() {
        return planRqstdept;
    }

    public void setPlanRqstdept(String planRqstdept) {
        this.planRqstdept = planRqstdept;
    }

    public String getFlightRule() {
        return flightRule;
    }

    public void setFlightRule(String flightRule) {
        this.flightRule = flightRule;
    }

    public String getFlightType() {
        return flightType;
    }

    public void setFlightType(String flightType) {
        this.flightType = flightType;
    }

    public String getPlanNumber() {
        return planNumber;
    }

    public void setPlanNumber(String planNumber) {
        this.planNumber = planNumber;
    }

    public String getAcrftType() {
        return acrftType;
    }

    public void setAcrftType(String acrftType) {
        this.acrftType = acrftType;
    }

    public String getWakeTurbcat() {
        return wakeTurbcat;
    }

    public void setWakeTurbcat(String wakeTurbcat) {
        this.wakeTurbcat = wakeTurbcat;
    }

    public String getPlanEquipment() {
        return planEquipment;
    }

    public void setPlanEquipment(String planEquipment) {
        this.planEquipment = planEquipment;
    }

    public String getPlanDeparture() {
        return planDeparture;
    }

    public void setPlanDeparture(String planDeparture) {
        this.planDeparture = planDeparture;
    }

    public String getPlanEtd() {
        return planEtd;
    }

    public void setPlanEtd(String planEtd) {
        this.planEtd = planEtd;
    }

    public String getPlanAtd() {
        return planAtd;
    }

    public void setPlanAtd(String planAtd) {
        this.planAtd = planAtd;
    }

    public String getCruisingSpeed() {
        return cruisingSpeed;
    }

    public void setCruisingSpeed(String cruisingSpeed) {
        this.cruisingSpeed = cruisingSpeed;
    }

    public String getFlightLevel() {
        return flightLevel;
    }

    public void setFlightLevel(String flightLevel) {
        this.flightLevel = flightLevel;
    }

    public String getPlanRoute() {
        return planRoute;
    }

    public void setPlanRoute(String planRoute) {
        this.planRoute = planRoute;
    }

    public String getPlanArrival() {
        return planArrival;
    }

    public void setPlanArrival(String planArrival) {
        this.planArrival = planArrival;
    }

    public String getOneAltn() {
        return oneAltn;
    }

    public void setOneAltn(String oneAltn) {
        this.oneAltn = oneAltn;
    }

    public String getTwoAltn() {
        return twoAltn;
    }

    public void setTwoAltn(String twoAltn) {
        this.twoAltn = twoAltn;
    }

    public String getPlanTeet() {
        return planTeet;
    }

    public void setPlanTeet(String planTeet) {
        this.planTeet = planTeet;
    }

    public String getPlanEta() {
        return planEta;
    }

    public void setPlanEta(String planEta) {
        this.planEta = planEta;
    }

    public String getPlanAta() {
        return planAta;
    }

    public void setPlanAta(String planAta) {
        this.planAta = planAta;
    }

    public String getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }

    public String getFlightPsbtime() {
        return flightPsbtime;
    }

    public void setFlightPsbtime(String flightPsbtime) {
        this.flightPsbtime = flightPsbtime;
    }

    public String getFlightPerson() {
        return flightPerson;
    }

    public void setFlightPerson(String flightPerson) {
        this.flightPerson = flightPerson;
    }

    public String getRrUhf() {
        return rrUhf;
    }

    public void setRrUhf(String rrUhf) {
        this.rrUhf = rrUhf;
    }

    public String getRrVhf() {
        return rrVhf;
    }

    public void setRrVhf(String rrVhf) {
        this.rrVhf = rrVhf;
    }

    public String getRrElt() {
        return rrElt;
    }

    public void setRrElt(String rrElt) {
        this.rrElt = rrElt;
    }

    public String getEmgcPolar() {
        return emgcPolar;
    }

    public void setEmgcPolar(String emgcPolar) {
        this.emgcPolar = emgcPolar;
    }

    public String getEmgcDesert() {
        return emgcDesert;
    }

    public void setEmgcDesert(String emgcDesert) {
        this.emgcDesert = emgcDesert;
    }

    public String getEmgcMaritime() {
        return emgcMaritime;
    }

    public void setEmgcMaritime(String emgcMaritime) {
        this.emgcMaritime = emgcMaritime;
    }

    public String getEmgcJungle() {
        return emgcJungle;
    }

    public void setEmgcJungle(String emgcJungle) {
        this.emgcJungle = emgcJungle;
    }

    public String getLifejkLight() {
        return lifejkLight;
    }

    public void setLifejkLight(String lifejkLight) {
        this.lifejkLight = lifejkLight;
    }

    public String getLifejkFluores() {
        return lifejkFluores;
    }

    public void setLifejkFluores(String lifejkFluores) {
        this.lifejkFluores = lifejkFluores;
    }

    public String getLifejkUhf() {
        return lifejkUhf;
    }

    public void setLifejkUhf(String lifejkUhf) {
        this.lifejkUhf = lifejkUhf;
    }

    public String getLifejkVhf() {
        return lifejkVhf;
    }

    public void setLifejkVhf(String lifejkVhf) {
        this.lifejkVhf = lifejkVhf;
    }

    public String getLifebtNumber() {
        return lifebtNumber;
    }

    public void setLifebtNumber(String lifebtNumber) {
        this.lifebtNumber = lifebtNumber;
    }

    public String getLifebtPerson() {
        return lifebtPerson;
    }

    public void setLifebtPerson(String lifebtPerson) {
        this.lifebtPerson = lifebtPerson;
    }

    public String getLifebtCover() {
        return lifebtCover;
    }

    public void setLifebtCover(String lifebtCover) {
        this.lifebtCover = lifebtCover;
    }

    public String getLifebtColor() {
        return lifebtColor;
    }

    public void setLifebtColor(String lifebtColor) {
        this.lifebtColor = lifebtColor;
    }

    public String getAcrftColor() {
        return acrftColor;
    }

    public void setAcrftColor(String acrftColor) {
        this.acrftColor = acrftColor;
    }

    public String getCaptainPhone() {
        return captainPhone;
    }

    public void setCaptainPhone(String captainPhone) {
        this.captainPhone = captainPhone;
    }

    public String getPlanPresent() {
        return planPresent;
    }

    public void setPlanPresent(String planPresent) {
        this.planPresent = planPresent;
    }

    public Integer getSaveType() {
        return saveType;
    }

    public void setSaveType(Integer saveType) {
        this.saveType = saveType;
    }

    public void realmSave(Realm realm){

        newSave(realm);

    }



    private void newSave(Realm realm){

        try {

            realm.beginTransaction();

            Number currentIdNum = realm.where(NhsFlightHistoryModel.class).max("idx");

            int nextId;
            if(currentIdNum == null) {
                nextId = 1;
            } else {
                nextId = currentIdNum.intValue() + 1;
            }

            this.idx = nextId;

            NhsFlightHistoryModel model = realm.createObject(NhsFlightHistoryModel.class, this.idx);
            model.setAcrftCd(getAcrftCd());
            model.setPlanDeparture(getPlanDeparture());
            model.setPlanArrival(getPlanArrival());
            model.setPlanTeet(getPlanTeet());
            model.setPlanDate(getPlanDate());
            model.setPlanRoute(getPlanRoute());
            model.setPlanId(getPlanId());
            model.setCallsign(getCallsign());
            model.setFlightType(getFlightType());
            model.setAcrftType(getAcrftType());
            model.setRegDate(getRegDate());
            model.setDepartureAerodrome(getPlanDeparture());
            model.setGpsLogDate(getGpsLogDate());
            model.setPlanSn(getPlanSn());
            model.setFlightId(getFlightId());
            model.setStartDate(getStartDate());
            model.setTotalDistanc(getTotalDistanc());
            model.setAvgSpeed(getAvgSpeed());
            model.setAvgAltitude(getAvgAltitude());
            model.setStartTime(getStartTime());
            model.setEndTime(getEndTime());
            model.setTotalFlightTime(getTotalFlightTime());
            model.setPlanDoccd(getPlanDoccd());

//            favorite.setName(name);
//            favorite.setStartData(startData);
//            favorite.setEndData(endData);
//            favorite.setRouteData(routeData);
            realm.commitTransaction();
        }catch (Exception ex) {
            ex.printStackTrace();
        } finally {

        }
    }


    private void editSave(Realm realm){
        // 객체 값 변경
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                NhsFlightHistoryModel model = realm.where(NhsFlightHistoryModel.class).equalTo("callsign", callsign).findFirst();
//                    model.setName(name);
//                    model.setStartData(startData);
//                    model.setEndData(endData);
//                    model.setRouteData(routeData);
            }
        });
    }

    public boolean realmUpdate() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(this);
        realm.commitTransaction();
        realm.close();
        return true;
    }

    public static ArrayList<NhsFlightHistoryModel> findAll(){
        Realm realm = Realm.getDefaultInstance();
        RealmResults<NhsFlightHistoryModel> flightPlanInfo = realm.where(NhsFlightHistoryModel.class).findAllSorted("idx", Sort.DESCENDING);

        ArrayList<NhsFlightHistoryModel> _flightPlanInfo = new ArrayList<NhsFlightHistoryModel>();
        for(NhsFlightHistoryModel model : flightPlanInfo){
            _flightPlanInfo.add(model);
        }
        return _flightPlanInfo;
    }

    public static List<NhsFlightHistoryModel> find(String callsign) {

        Realm realm = Realm.getDefaultInstance();
        RealmQuery<NhsFlightHistoryModel> query;

        if (callsign.isEmpty()) {
            query = realm.where(NhsFlightHistoryModel.class);
        } else {
//            query = realm.where(NhsFlightHistoryModel.class).like("departureAerodrome", "*" + departureAerodrome + "*");
            query = realm.where(NhsFlightHistoryModel.class).like("callsign", "*" + callsign + "*");
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        long today = new Date().getTime();
//        try {
//            today = format.parse(format.format(System.currentTimeMillis())).getTime();
//        } catch (ParseException e1) {
//            today = System.currentTimeMillis();
//        }

        query = query.between("regDate", today - (24 * 60 * 60 * 1000), today);

        return query.findAll();

    }

    public static List<NhsFlightHistoryModel> findCodeWithDate(String code) {

        Realm realm = Realm.getDefaultInstance();
        RealmQuery<NhsFlightHistoryModel> query;

        if (code.isEmpty()) {
            query = realm.where(NhsFlightHistoryModel.class);
        } else {
           query = realm.where(NhsFlightHistoryModel.class).like("planArrival", "*" + code + "*").or().like("departureAerodrome", "*" + code + "*").or().like("planDate", "*" + code + "*");
        }

        return query.findAll();

    }

    public static List<NhsFlightHistoryModel> find()
    {
        List<NhsFlightHistoryModel> list = new ArrayList<NhsFlightHistoryModel>();
        Realm realm = Realm.getDefaultInstance();
        RealmResults<NhsFlightHistoryModel> results = realm.where(NhsFlightHistoryModel.class).findAll();
        for(int i = 0 ; i < results.size() ; i++){
            list.add(results.get(i));
        }
        realm.close();
        return list;
    }

    /**
     * 삭제
     * @author FIESTA
     * @since  오전 12:49
     **/
    public static void delete(final long idx) {

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                RealmResults<NhsFlightHistoryModel> result = realm.where(NhsFlightHistoryModel.class).equalTo("idx",idx).findAll();
                result.deleteAllFromRealm();

            }
        });

    }
}
