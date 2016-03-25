package com.hawk.gshassist.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.hawk.gshassist.R;
import com.hawk.gshassist.adapter.BookInfoAdapter;
import com.hawk.gshassist.model.BookInfo;
import com.hawk.gshassist.util.GSHNets;
import com.hawk.gshassist.util.ToastUtil;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchBookFragment extends BaseFragment implements View.OnClickListener{
    private EditText fsbok_book;
    private Button fsbok_search;
    private ListView fsbok_list;
    private BookInfoAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layoutView=inflater.inflate(R.layout.fragment_search_book,null);
        fsbok_book= (EditText) layoutView.findViewById(R.id.fsbok_book);
        fsbok_search= (Button) layoutView.findViewById(R.id.fsbok_search);
        fsbok_list= (ListView) layoutView.findViewById(R.id.fsbok_list);
        adapter=new BookInfoAdapter(getContext(),null);
        fsbok_list.setAdapter(adapter);

        fsbok_search.setOnClickListener(this);

        return layoutView;
    }

    private void searchBooks(){
        String keywords = fsbok_book.getText().toString();
        if(TextUtils.isEmpty(keywords)){
            ToastUtil.show("请输入关键字");
            return;
        }
        mListener.showProgress(null,"正在查询数据,请稍候...");
        final String url = GSHNets.NET_LIB_SEARCHBOOK.replace("@keywords@", keywords);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Connection.Response rps = Jsoup.connect(url)
                            .method(Connection.Method.GET)
                            .followRedirects(true)
                            .execute();

                    List<BookInfo> booksList=new ArrayList<BookInfo>();
                    Document doc=rps.parse();
                    Element elem=doc.select("tbody").first();
                    Elements trs=elem.select("tr");
                    for(int i=0;i<trs.size();i++){
                        Element tr=trs.get(i);
                        Elements tds=tr.select("td");

                        BookInfo item=new BookInfo();
                        item.setTiming(tds.get(1).text());
                        item.setZerenzhe(tds.get(2).text());
                        item.setChubanshe(tds.get(3).text());
                        item.setChubannian(tds.get(4).text());
                        item.setSuoquhao(tds.get(5).text());
                        item.setGuancang(tds.get(6).text());
                        item.setKejie(tds.get(7).text());
                        item.setXiangguanziyuan(tds.get(8).text());

                        booksList.add(item);
                    }
                    showBooks(booksList);
                } catch (IOException e) {
                    e.printStackTrace();
                    mListener.dismissProgress();
                    ToastUtil.show("获取数据失败");
                }
            }
        }).start();
    }

    private void showBooks(final List<BookInfo> booksList){
        getMainActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mListener.dismissProgress();
                adapter.setList(booksList);
            }
        });
    }

    @Override
    public void onClick(View v) {
        searchBooks();
    }

    @Override
    public void refreshFragment() {
        searchBooks();
    }
}
