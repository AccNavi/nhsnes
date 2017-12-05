package kr.go.molit.nhsnes.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.adapter.RecyclerModifyRoute;
import kr.go.molit.nhsnes.interfaces.OnSearchWayPointListener;
import kr.go.molit.nhsnes.interfaces.OnStartDragListener;
import kr.go.molit.nhsnes.interfaces.SimpleItemTouchHelperCallback;
import kr.go.molit.nhsnes.widget.TextViewEx;


/**
 * 화면 검색에서 경로 수정을 위한 다이얼로그
 *
 * @author FIESTA
 * @since 오후 8:02
 **/
public class DialogModifyRoute extends DialogBase implements View.OnClickListener, OnStartDragListener {

  private OnSearchWayPointListener onSearchWayPointListener;
  private View.OnClickListener onClickListener;
  private int addWayPointCnt = 0;
  private ItemTouchHelper mItemTouchHelper;
  private Context context;
  private List<String> routes = null;
  private IRouteResultListener mIRouteResultListener;

  public DialogModifyRoute(@NonNull Context context, int mode, View.OnClickListener onClickListener) {
    super(context);
    this.context = context;
    this.onClickListener = onClickListener;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.dialog_modify_route);

    TextViewEx tvOk = (TextViewEx) findViewById(R.id.tv_menu2);
    TextViewEx tvTitle = (TextViewEx) findViewById(R.id.tv_title);
    TextViewEx tvCancel = (TextViewEx) findViewById(R.id.tv_menu3);

    findViewById(R.id.btn_complate).setOnClickListener(this);
    findViewById(R.id.btn_cancel).setOnClickListener(this.onClickListener);

    setupList();

    setOnDismissListener(new DialogInterface.OnDismissListener() {
      @Override
      public void onDismiss(DialogInterface dialogInterface) {
        if (isPushButton) {
          DialogModifyRoute.this.onClick(findViewById(R.id.btn_complate));
        }
      }
    });

  }

  /**
   * 경로 설정
   * \n 으로 구분한다.
   *
   * @author FIESTA
   * @since 오전 9:19
   **/
  public void setRoutes(String routes) {
    Log.i("TEST","routes:::"+routes);
    if(null != routes && routes.length() > 0 ){
      this.routes = new LinkedList<String>(Arrays.asList(routes.split("\\n")));
    } else {
      this.routes = new LinkedList<String>();
    }

  }

  public void setRoutesListener(IRouteResultListener listener){
    mIRouteResultListener = listener;
  }

  private void setupList() {

    RecyclerModifyRoute adapter = new RecyclerModifyRoute(this.context, this);
    adapter.setItems(this.routes);
    adapter.setRoutesListener(mIRouteResultListener);

    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_list);
    recyclerView.setHasFixedSize(true);
    recyclerView.setAdapter(adapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this.context));

    ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
    mItemTouchHelper = new ItemTouchHelper(callback);
    mItemTouchHelper.attachToRecyclerView(recyclerView);
  }

  public int getAddWayPointCnt() {
    return addWayPointCnt;
  }

  public void setAddWayPointCnt(int addWayPointCnt) {
    this.addWayPointCnt = addWayPointCnt;
  }


  @Override
  public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
    mItemTouchHelper.startDrag(viewHolder);
  }

  @Override
  public void onClick(View view) {
    view.setTag(this.routes);
    this.onClickListener.onClick(view);
  }


  /**
   * Routes데이터 Acticity로 반환
   */
  public interface IRouteResultListener {
    void onResult(String routes);
  }

}