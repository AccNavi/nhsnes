package kr.go.molit.nhsnes.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import kr.go.molit.nhsnes.Network.NetworkProcess;
import kr.go.molit.nhsnes.Network.NetworkProcessWithFile;
import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.activity.NhsAipInfoActivity;
import kr.go.molit.nhsnes.activity.NhsAirlineChartActivity;
import kr.go.molit.nhsnes.activity.NhsAirlineInquiryActivity;
import kr.go.molit.nhsnes.common.NetworkParamUtil;
import kr.go.molit.nhsnes.common.NetworkUrlUtil;
import kr.go.molit.nhsnes.common.StorageUtil;
import kr.go.molit.nhsnes.common.ToastUtile;
import kr.go.molit.nhsnes.common.Util;
import kr.go.molit.nhsnes.dialog.DialogType1;
import kr.go.molit.nhsnes.model.NhsAirListModel;
import kr.go.molit.nhsnes.model.NhsFlightDeleteModel;
import kr.go.molit.nhsnes.model.NhsFlightInfoModel;
import kr.go.molit.nhsnes.model.NhsFlightWeatherMetarModel;
import kr.go.molit.nhsnes.model.NhsNGllDataModel;
import kr.go.molit.nhsnes.net.model.AirChartListItemModel;
import kr.go.molit.nhsnes.widget.CustomViewListType1;

import static kr.go.molit.nhsnes.activity.NhsLoginActivity.LOGIN_TOKEN_KEY;


public class RecyclerNhsFlightInfoListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Activity activity;

    private ArrayList<Object> nhsFlightDownloadModel;
    private int viewType;
    public static final int VIEWTYPE_TITLE_DOWNLOAD = 0;
    public static final int VIEWTYPE_TITLE_DATE_DELETE = 1;
    public static final int VIEWTYPE_TITLE_DATE_DOWNLOAD_DELETE = 2;
    public static final int VIEWTYPE_TITLE_DATE_DOWNLOAD = 3;

    private NetworkProcessWithFile.OnResultListener onFileResultListener;


    public static final String INTENT_ID = "INTENT_ID";
    private DialogType1 deleteDialog2;


    public RecyclerNhsFlightInfoListAdapter(Activity activity) {
        this.activity = activity;
        this.viewType = RecyclerNhsFlightInfoListAdapter.VIEWTYPE_TITLE_DOWNLOAD;
    }

    public RecyclerNhsFlightInfoListAdapter(Activity activity, int ViewType) {
        this.activity = activity;
        this.viewType = ViewType;
    }

    public void addData(Object model) {

        if (this.nhsFlightDownloadModel == null) {
            this.nhsFlightDownloadModel = new ArrayList<>();
        }

        this.nhsFlightDownloadModel.add(model);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NhsFlightListHolderType1(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_nhs_airline_info, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final NhsFlightInfoModel result = (NhsFlightInfoModel) nhsFlightDownloadModel.get(position);
        final NhsFlightListHolderType1 type1 = (NhsFlightListHolderType1) holder;

        if (viewType == VIEWTYPE_TITLE_DOWNLOAD) {

            final AirChartListItemModel airResult = result.getAirChartListItemModel();


            type1.bgLayout.setTextViewText(airResult.getAIRCHART_GB() + " " + airResult.getAIRCHART_VER() + " " + airResult.getFILE_ORIGIN_NM());
            type1.bgLayout.setImageViewPreVisible(true);

            type1.bgLayout.setOnLayoutClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = null;
                    if (activity instanceof NhsAipInfoActivity) {
                        i = new Intent(activity, NhsAipInfoActivity.class);
                        i.putExtra(INTENT_ID, airResult.getAIRCHART_SN());
                        activity.startActivity(i);
                    } else if (activity instanceof NhsAirlineInquiryActivity) {
                        i = new Intent(activity, NhsAirlineInquiryActivity.class);
                        i.putExtra(INTENT_ID, airResult.getAIRCHART_SN());
                        activity.startActivity(i);
                    } else if (activity instanceof NhsAirlineChartActivity) {
                        AirChartListItemModel dataModel = (AirChartListItemModel) v.getTag();
                        downloadChartFile(Environment.getExternalStorageDirectory() + "/ACC_NAVI/Flight_Char", dataModel.getFILE_NM(), dataModel.getFILE_ORIGIN_NM());
                    }

                }
            }, airResult);

        } else if (viewType == VIEWTYPE_TITLE_DATE_DELETE) {

            if (result instanceof NhsFlightDeleteModel) {

                final NhsFlightDeleteModel convertResult = (NhsFlightDeleteModel) result;

                type1.bgLayout.setTextViewText(convertResult.getFileName());
                type1.bgLayout.setTextViewSubText(convertResult.getDate());
                type1.bgLayout.setImageViewPostVisible(true);
                type1.bgLayout.setOnLayoutClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        Uri uri = Uri.fromFile(convertResult.getFile());
                        i.setDataAndType(uri, Util.getMimeType(convertResult.getFile().getPath()));
                        activity.startActivity(Intent.createChooser(i, "Open"));
                    }
                });
                type1.bgLayout.setOnPostIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteDialog2 = new DialogType1(activity, "삭제 확인", convertResult.getFileName() + "를\n 삭제하시겠습니까?", activity.getString(R.string.btn_confirm), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteDialog2.hideDialog();
                                Toast.makeText(activity, "삭제", Toast.LENGTH_SHORT).show();
                                // TODO: 2017. 4. 24. 삭제 기능 추가+항공정보이름 기재하기
                                if (convertResult.getFile() != null) {
                                    convertResult.getFile().delete();
                                    nhsFlightDownloadModel.remove(result);
                                    notifyDataSetChanged();
                                }

                            }
                        }, activity.getString(R.string.btn_cancel), new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                deleteDialog2.hideDialog();
                            }
                        });

                        notifyDataSetChanged();
                    }
                });


            }


        } else if (viewType == VIEWTYPE_TITLE_DATE_DOWNLOAD_DELETE) {

            if (result instanceof NhsAirListModel) {

                final NhsAirListModel convertResult = (NhsAirListModel) result;

                type1.bgLayout.setTextViewText(convertResult.getTitle());
                type1.bgLayout.setTextViewSubText("");

                if (convertResult.getFile().exists()) {
                    type1.bgLayout.setImageViewPostVisible(true);
                } else {
                    type1.bgLayout.setImageViewPostVisible(false);
                }

                type1.bgLayout.setImageViewPreVisible(true);
                type1.bgLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        File file = new File(convertResult.getSavePath() + convertResult.getFileName());

                        // 파일이 존재하면 연다.
                        if (file.exists()) {

                            String mime = Util.getMimeType(file.getPath());

                            if (!mime.isEmpty() && (mime.equalsIgnoreCase("image/jpeg")
                                    || mime.equalsIgnoreCase("application/pdf"))) {
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                Uri uri = Uri.fromFile(file);
                                i.setDataAndType(uri, Util.getMimeType(file.getPath()));
                                activity.startActivity(Intent.createChooser(i, "Open"));
                            }
                        }

                    }
                });

                type1.bgLayout.setOnPreIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        downloadChartFileWithUrl(convertResult.getSavePath(), convertResult.getFileName(), convertResult.getResultUrl());
                    }
                });
                type1.bgLayout.setOnPostIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            File file = convertResult.getFile();
                            file.delete();
                        } catch (Exception ex) {

                        } finally {
                            new ToastUtile().showCenterText(activity, "삭제되었습니다.");
                            notifyDataSetChanged();
                        }
                    }
                });

            } else if (result instanceof NhsNGllDataModel) {

                final NhsNGllDataModel convertResult = (NhsNGllDataModel) result;

                type1.bgLayout.setTextViewText(convertResult.getTitle());
                type1.bgLayout.setTextViewSubText("");

                if (convertResult.getFile().exists()) {
                    type1.bgLayout.setImageViewPostVisible(true);
                } else {
                    type1.bgLayout.setImageViewPostVisible(false);
                }

                type1.bgLayout.setImageViewPreVisible(true);


                type1.bgLayout.setOnPreIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        downloadChartFileDoyup(convertResult.getResultUrl(), "5", convertResult.getFileName(), convertResult.getSavePath());
//                        downloadChartFileWithUrl(convertResult.getSavePath(), convertResult.getFileName(), convertResult.getResultUrl());
                    }
                });
                type1.bgLayout.setOnPostIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            File file = convertResult.getFile();
                            file.delete();
                        } catch (Exception ex) {

                        } finally {
                            new ToastUtile().showCenterText(activity, "삭제되었습니다.");
                            notifyDataSetChanged();
                        }
                    }
                });


//
//                type1.bgLayout.setTextViewText(convertResult.getFileName());
//                type1.bgLayout.setTextViewSubText("");
//
//                final File realFile = new File(Environment.getExternalStorageDirectory().toString() + "/ACC_NAVI/Map_Data/" + convertResult.getFileName());
//
//                if (realFile.exists()) {
//                    type1.bgLayout.setImageViewPostVisible(true);
//                } else {
//                    type1.bgLayout.setImageViewPostVisible(false);
//                }
//
//                type1.bgLayout.setImageViewPreVisible(true);

//                type1.bgLayout.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        File file = new File(Environment.getExternalStorageDirectory().toString() + "/ACC_NAVI/Map_Data/" + convertResult.getFileName());
//
//                        // 파일이 존재하면 연다.
//                        if (file.exists()) {
//                            Intent i = new Intent(Intent.ACTION_VIEW);
//                            Uri uri = Uri.fromFile(file);
//                            i.setDataAndType(uri, Util.getMimeType(file.getPath()));
//                            activity.startActivity(Intent.createChooser(i, "Open"));
//                        }
//
//                    }
//                });
//
//                type1.bgLayout.setOnPreIconClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        try {
//                            Util.copyFile(convertResult.getFile(), new File(Environment.getExternalStorageDirectory().toString() + "/ACC_NAVI/Map_Data/" + convertResult.getFileName()));
//                            new ToastUtile().showCenterText(activity, "다운로드 완료");
//                            notifyDataSetChanged();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                });
//                type1.bgLayout.setOnPostIconClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        try {
//                            realFile.delete();
//                        } catch (Exception ex) {
//
//                        } finally {
//                            new ToastUtile().showCenterText(activity, "삭제되었습니다.");
//                            notifyDataSetChanged();
//                        }
//                    }
//                });

            } else if (result instanceof NhsFlightWeatherMetarModel) {

                final NhsFlightWeatherMetarModel nhsFlightWeatherMetar = (NhsFlightWeatherMetarModel) result;

                type1.bgLayout.setTextViewText(nhsFlightWeatherMetar.getFileName());

                final File file = new File(result.getSavePath() + nhsFlightWeatherMetar.getFileName());
                if (file.exists()) {
                    type1.bgLayout.setImageViewPostVisible(true);
                } else {
                    type1.bgLayout.setImageViewPostVisible(false);
                }

                type1.bgLayout.setImageViewPreVisible(true);

                type1.bgLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        File file = new File(nhsFlightWeatherMetar.getSavePath() + nhsFlightWeatherMetar.getFileName());

                        // 파일이 존재하면 연다.
                        if (file.exists()) {
                            String mime = Util.getMimeType(file.getPath());

                            if (!mime.isEmpty() && (mime.equalsIgnoreCase("image/jpeg")
                                    || mime.equalsIgnoreCase("application/pdf"))) {
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                Uri uri = Uri.fromFile(file);
                                i.setDataAndType(uri, Util.getMimeType(file.getPath()));
                                activity.startActivity(Intent.createChooser(i, "Open"));
                            }

                        }

                    }
                });

                type1.bgLayout.setOnLayoutClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        NetworkUrlUtil networkUrlUtil = new NetworkUrlUtil();
                        NetworkParamUtil networkParamUtil = new NetworkParamUtil();

                        StringEntity param = networkParamUtil.getWeather("", StorageUtil.getStorageModeEx(activity, LOGIN_TOKEN_KEY, ""), "", "", "");
                        NetworkProcess networkProcess = new NetworkProcess(activity,
                                networkUrlUtil.getWeatherMetar(),
                                param,
                                new NetworkProcess.OnResultListener() {
                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                        try {

                                            new ToastUtile().showCenterText(activity, activity.getString(R.string.error_network) + "(" + statusCode + ")");

                                        } catch (Exception ex) {

                                        } finally {
                                        }
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                        try {

                                            new ToastUtile().showCenterText(activity, activity.getString(R.string.error_network) + "(" + statusCode + ")");

                                        } catch (Exception ex) {

                                        } finally {
                                        }
                                    }

                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                                        String msg = response.optString("result_msg");
                                        String resultCode = response.optString("result_code");

                                        if (resultCode.equalsIgnoreCase("Y")) {

                                            // 파일로 저장
                                            Util.appendStringAsFile(nhsFlightWeatherMetar.getSavePath(),
                                                    nhsFlightWeatherMetar.getFileName(),
                                                    response.optString("result_data", ""));

                                            if (onFileResultListener != null) {
                                                onFileResultListener.onSuccess(new File(nhsFlightWeatherMetar.getSavePath() + nhsFlightWeatherMetar.getFileName()));
                                            }
                                        }

                                    }

                                }, true);
                        networkProcess.sendEmptyMessage(0);


                    }
                }, nhsFlightWeatherMetar);

                type1.bgLayout.setOnPostIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            file.delete();
                        } catch (Exception ex) {

                        } finally {
                            new ToastUtile().showCenterText(activity, "삭제되었습니다.");
                            notifyDataSetChanged();
                        }
                    }
                });


            } else if (result instanceof NhsFlightInfoModel) {

                final AirChartListItemModel airResult = result.getAirChartListItemModel();

                if (airResult != null) {
                    type1.bgLayout.setTextViewText(airResult.getAIRCHART_GB() + " " + airResult.getAIRCHART_VER() + " " + airResult.getFILE_ORIGIN_NM());

                    final File file = new File(result.getSavePath() + airResult.getFILE_ORIGIN_NM());
                    if (file.exists()) {
                        type1.bgLayout.setImageViewPostVisible(true);
                    } else {
                        type1.bgLayout.setImageViewPostVisible(false);
                    }

                    type1.bgLayout.setImageViewPreVisible(true);

                    type1.bgLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                        }
                    });

                    type1.bgLayout.setOnLayoutClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            // 파일이 존재하면 연다.
                            if (file.exists()) {
                                String mime = Util.getMimeType(file.getPath());

                                if (!mime.isEmpty() && (mime.equalsIgnoreCase("image/jpeg")
                                        || mime.equalsIgnoreCase("application/pdf"))) {
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    Uri uri = Uri.fromFile(file);
                                    i.setDataAndType(uri, Util.getMimeType(file.getPath()));
                                    activity.startActivity(Intent.createChooser(i, "Open"));
                                }
                            } else {

                                AirChartListItemModel dataModel = (AirChartListItemModel) v.getTag();
                                downloadChartFile(result.getSavePath(), dataModel.getFILE_NM(), dataModel.getFILE_ORIGIN_NM());

                            }


                        }
                    }, airResult);

                    type1.bgLayout.setOnPostIconClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                file.delete();
                            } catch (Exception ex) {

                            } finally {
                                new ToastUtile().showCenterText(activity, "삭제되었습니다.");
                                notifyDataSetChanged();
                            }
                        }
                    });
                }

            }

        } else if (viewType == VIEWTYPE_TITLE_DATE_DOWNLOAD) {

            final AirChartListItemModel airResult = result.getAirChartListItemModel();

            type1.bgLayout.setTextViewText(airResult.getAIRCHART_GB() + " " + airResult.getAIRCHART_VER() + " " + airResult.getFILE_ORIGIN_NM());
            type1.bgLayout.setTextViewSubText("");
            type1.bgLayout.setImageViewPreVisible(true);
            type1.bgLayout.setOnPreIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }


    }

    public ArrayList<Object> getAllData() {
        return nhsFlightDownloadModel;
    }


    @Override
    public int getItemCount() {
        if (this.nhsFlightDownloadModel == null) {
            return 0;
        }
        return this.nhsFlightDownloadModel.size();
    }

    public class NhsFlightListHolderType1 extends RecyclerView.ViewHolder {
        CustomViewListType1 bgLayout;

        public NhsFlightListHolderType1(View itemView) {
            super(itemView);
            bgLayout = (CustomViewListType1) itemView.findViewById(R.id.bg_layout);

        }
    }

    public NetworkProcessWithFile.OnResultListener getOnFileResultListener() {
        return onFileResultListener;
    }

    public void setOnFileResultListener(NetworkProcessWithFile.OnResultListener onFileResultListener) {
        this.onFileResultListener = onFileResultListener;
    }

    /**
     * 파일을 다운로드한다.
     *
     * @author FIESTA
     * @version 1.0.0
     * @since 2017-09-07 오후 4:04
     **/
    private void downloadChartFile(String downloadPath, String fileName, String fileOriginName) {

        org.apache.http.entity.StringEntity param = new NetworkParamUtil().requestDownload(this.activity, fileName, fileOriginName);
        NetworkProcessWithFile downloadFile = new NetworkProcessWithFile(this.activity, new NetworkUrlUtil().getRequestDownload(), param, downloadPath, fileOriginName, this.onFileResultListener, true);
        downloadFile.execute();

    }

    /**
     * 파일을 다운로드한다.
     *
     * @author FIESTA
     * @version 1.0.0
     * @since 2017-09-07 오후 4:04
     **/
    private void downloadChartFileDoyup(String downloadPath, String mapType, String fileOriginName, String savePath) {

        org.apache.http.entity.StringEntity param = new NetworkParamUtil().requestDownloadDoyup(this.activity, mapType, downloadPath);
        NetworkProcessWithFile downloadFile = new NetworkProcessWithFile(this.activity, new NetworkUrlUtil().getDownloadDoyup(), param, savePath, fileOriginName, this.onFileResultListener, true);
        downloadFile.execute();

    }


    /**
     * 파일을 다운로드한다.
     *
     * @author FIESTA
     * @version 1.0.0
     * @since 2017-09-07 오후 4:04
     **/
    private void downloadChartFileWithUrl(String downloadPath, String fileName, String url) {

        NetworkProcessWithFile downloadFile = new NetworkProcessWithFile(this.activity, url, null, downloadPath, fileName, this.onFileResultListener, true);
        downloadFile.execute();

    }

}
