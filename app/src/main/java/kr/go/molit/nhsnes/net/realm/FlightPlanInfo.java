package kr.go.molit.nhsnes.net.realm;

import android.content.Context;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.annotations.PrimaryKey;
import kr.go.molit.nhsnes.model.NhsFlightPlainModel;

/**
 * Created by user on 2017-08-17.
 */
@SuppressWarnings("serial")
public class FlightPlanInfo extends RealmObject implements Serializable {

    /**
    * db 저장 결과값 반환 리스너
    * @author FIESTA
    * @since  오후 11:00
    **/
    public interface OnResultListener{
        public void onResult(boolean isSuccess);
    }

    public final static String TYPE_MESSAGE_CHG = "CHG";        // 수정
    public final static String TYPE_MESSAGE_FPL = "FPL";        // 신규

    public enum FindType {
        TMP //임시저장
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
    private Integer saveType;
    private String flightId;
    private long createAt;
    private long gpsLogDate;
    private String startDate;   // 시작 일자
    private long totalDistanc;  //  총 비행 거리
    private int avgSpeed;       // 평균 속도
    private int avgAltitude;  // 평균 고도
    private String startTime;   // 시작 시간
    private String endTime;     // 도착 시간
    private long totalFlightTime;   // 총 비행 시간(밀리세컨드)

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

    public long getTotalFlightTime() {
        return totalFlightTime;
    }

    public void setTotalFlightTime(long totalFlightTime) {
        this.totalFlightTime = totalFlightTime;
    }

    public long getGpsLogDate() {
        return gpsLogDate;
    }

    public void setGpsLogDate(long gpsLogDate) {
        this.gpsLogDate = gpsLogDate;
    }

    public long getIdx() {
        return idx;
    }

    public void setIdx(long idx) {
        this.idx = idx;
    }
    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
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

    public long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(long createAt) {
        this.createAt = createAt;
    }

    public void realmSave(Realm realm){

//        List<FlightPlanInfo> list = realm.where(FlightPlanInfo.class).findAllSorted("callsign", Sort.DESCENDING);


//        realm.close();

        if(getMessageType() == TYPE_MESSAGE_CHG){
            editSave(realm);
        } else {
            newSave(realm);
        }
    }

    public void realmSave(Realm realm, OnResultListener onResultListener){

        if(getMessageType() == TYPE_MESSAGE_CHG){
            editSave(realm);
        } else {
            newSave(realm);
        }
    }



    private void newSave(Realm realm){

        realm.executeTransaction(new Realm.Transaction(){

            @Override
            public void execute(Realm realm) {

                try {
                    FlightPlanInfo model = realm.createObject(FlightPlanInfo.class, callsign);
                    setData(model);
                }catch (io.realm.exceptions.RealmPrimaryKeyConstraintException keyEx) {



                } finally {

                }
            }
        });

    }

    /**
    * 수정
    * @author FIESTA
    * @since  오후 11:03
    **/
    private void editSave(Realm realm, final OnResultListener onResultListener){
        // 객체 값 변경
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                boolean isSucc = false;

                try {
                    FlightPlanInfo model = realm.where(FlightPlanInfo.class).equalTo("callsign", callsign).findFirst();
                    setData(model);
                    isSucc= true;
                }catch (Exception ex) {
                    isSucc = false;
                } finally {
                    if (onResultListener != null) {
                        onResultListener.onResult(isSucc);
                    }
                }
            }
        });
    }

    private void editSave(Realm realm){
        // 객체 값 변경
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                FlightPlanInfo model = realm.where(FlightPlanInfo.class).equalTo("callsign", callsign).findFirst();
                setData(model);
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

    public ArrayList<FlightPlanInfo> findAll(){
        Realm realm = Realm.getDefaultInstance();
        RealmResults<FlightPlanInfo> flightPlanInfo = realm.where(FlightPlanInfo.class).findAll();

        ArrayList<FlightPlanInfo> _flightPlanInfo = new ArrayList<FlightPlanInfo>();
        for(FlightPlanInfo model : flightPlanInfo){
            _flightPlanInfo.add(model);
        }
        return _flightPlanInfo;
    }

    public List<FlightPlanInfo> find(int type)
    {
        List<FlightPlanInfo> list = new ArrayList<FlightPlanInfo>();
        Realm realm = Realm.getDefaultInstance();
        RealmResults<FlightPlanInfo> results = realm.where(FlightPlanInfo.class).findAll();
        for(int i = 0 ; i < results.size() ; i++){
            list.add(results.get(i));
        }
        realm.close();
        return list;
    }

    public FlightPlanInfo find(String _callsign)
    {
        FlightPlanInfo info = null;
        Realm realm = Realm.getDefaultInstance();
        RealmResults<FlightPlanInfo> results = realm.where(FlightPlanInfo.class).findAll();
        for(int i = 0 ; i < results.size() ; i++){
            if(null != results.get(i).getCallsign() && results.get(i).getCallsign().equals(_callsign)){
                info = results.get(i);
            }
        }

        realm.close();
        return info;
    }


    public void delete(){
        Realm realm = Realm.getDefaultInstance();

        // 데이터에 대한 모든 변경은 트랜잭션에서 이루어져야 합니다
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                deleteFromRealm();
            }
        });

    }

    public void setData(FlightPlanInfo data){

        if (data != null) {
            data.setPlanId(planId);
            data.setPlanSn(planSn);
            data.setAcrftCd(acrftCd);
            data.setPlanStatus(planStatus);
            data.setPlanDoccd(planDoccd);
            data.setMessageType(messageType);
            data.setPlanPurpose(planPurpose);
            data.setPlanMbrId(planMbrId);
            data.setPlanDate(planDate);
            data.setPlanArvDate(planArvDate);
            data.setPlanRfsDate(planRfsDate);
            data.setPlanPriority(planPriority);
            data.setPlanFltm(planFltm);
            data.setPlanRqstdept(planRqstdept);
            data.setFlightRule(flightRule);
            data.setFlightType(flightType);
            data.setPlanNumber(planNumber);
            data.setAcrftType(acrftType);
            data.setWakeTurbcat(wakeTurbcat);
            data.setPlanEquipment(planEquipment);
            data.setPlanDeparture(planDeparture);
            data.setPlanEtd(planEtd);
            data.setPlanAtd(planAtd);
            data.setCruisingSpeed(cruisingSpeed);
            data.setFlightLevel(flightLevel);
            data.setPlanRoute(planRoute);
            data.setPlanArrival(planArrival);
            data.setOneAltn(oneAltn);
            data.setTwoAltn(twoAltn);
            data.setPlanTeet(planTeet);
            data.setPlanEta(planEta);
            data.setPlanAta(planAta);
            data.setOtherInfo(otherInfo);
            data.setFlightPsbtime(flightPsbtime);
            data.setFlightPerson(flightPerson);
            data.setRrUhf(rrUhf);
            data.setRrVhf(rrVhf);
            data.setRrElt(rrElt);
            data.setEmgcPolar(emgcPolar);
            data.setEmgcDesert(emgcDesert);
            data.setEmgcMaritime(emgcMaritime);
            data.setEmgcJungle(emgcJungle);
            data.setLifejkLight(lifejkLight);
            data.setLifejkFluores(lifejkFluores);
            data.setLifejkUhf(lifejkUhf);
            data.setLifejkVhf(lifejkVhf);
            data.setLifebtNumber(lifebtNumber);
            data.setLifebtPerson(lifebtPerson);
            data.setLifebtCover(lifebtCover);
            data.setLifebtColor(lifebtColor);
            data.setAcrftColor(acrftColor);
            data.setCaptainPhone(captainPhone);
            data.setPlanPresent(planPresent);
            data.setSaveType(saveType);
        } else {
            data.setPlanId(data.getPlanId());
            data.setPlanSn(data.getPlanSn());
            data.setAcrftCd(data.getAcrftCd());
            data.setPlanStatus(data.getPlanStatus());
            data.setPlanDoccd(data.getPlanDoccd());
            data.setMessageType(data.getMessageType());
            data.setPlanPurpose(data.getPlanPurpose());
            data.setPlanMbrId(data.getPlanMbrId());
            data.setPlanDate(data.getPlanDate());
            data.setPlanArvDate(data.getPlanAtd());
            data.setPlanRfsDate(data.getPlanRfsDate());
            data.setPlanPriority(data.getPlanPriority());
            data.setPlanFltm(data.getPlanFltm());
            data.setPlanRqstdept(data.getPlanRqstdept());
            data.setFlightRule(data.getFlightRule());
            data.setFlightType(data.getFlightType());
            data.setPlanNumber(data.getPlanNumber());
            data.setAcrftType(data.getAcrftType());
            data.setWakeTurbcat(data.getWakeTurbcat());
            data.setPlanEquipment(data.getPlanEquipment());
            data.setPlanDeparture(data.getPlanDeparture());
            data.setPlanEtd(data.getPlanEtd());
            data.setPlanAtd(data.getPlanAtd());
            data.setCruisingSpeed(data.getCruisingSpeed());
            data.setFlightLevel(data.getFlightLevel());
            data.setPlanRoute(data.getPlanRoute());
            data.setPlanArrival(data.getPlanArrival());
            data.setOneAltn(data.getOneAltn());
            data.setTwoAltn(data.getTwoAltn());
            data.setPlanTeet(data.getPlanTeet());
            data.setPlanEta(data.getPlanEtd());
            data.setPlanAta(data.getPlanAta());
            data.setOtherInfo(data.getOtherInfo());
            data.setFlightPsbtime(data.getFlightPsbtime());
            data.setFlightPerson(data.getFlightPerson());
            data.setRrUhf(data.getRrUhf());
            data.setRrVhf(data.getRrVhf());
            data.setRrElt(data.getRrElt());
            data.setEmgcPolar(data.getEmgcPolar());
            data.setEmgcDesert(data.getEmgcDesert());
            data.setEmgcMaritime(data.getEmgcMaritime());
            data.setEmgcJungle(data.getEmgcJungle());
            data.setLifejkLight(data.getLifejkLight());
            data.setLifejkFluores(data.getLifejkFluores());
            data.setLifejkUhf(data.getLifejkUhf());
            data.setLifejkVhf(data.getLifejkVhf());
            data.setLifebtNumber(data.getLifebtNumber());
            data.setLifebtPerson(data.getLifebtPerson());
            data.setLifebtCover(data.getLifebtCover());
            data.setLifebtColor(data.getLifebtColor());
            data.setAcrftColor(data.getAcrftColor());
            data.setCaptainPhone(data.getCaptainPhone());
            data.setPlanPresent(data.getPlanPresent());
            data.setSaveType(data.getSaveType());
        }

        data.setCreateAt(Calendar.getInstance().getTimeInMillis());

    }
}
