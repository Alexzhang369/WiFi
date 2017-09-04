package com.codingbingoZTT.BanmaReader.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.codingbingoZTT.BanmaReader.Constants;
import com.codingbingoZTT.BanmaReader.R;
import com.codingbingoZTT.BanmaReader.base.BaseActivity;
import com.codingbingoZTT.BanmaReader.base.BaseFragment;
import com.codingbingoZTT.BanmaReader.dao.Book;
import com.codingbingoZTT.BanmaReader.dao.BookDao;
import com.codingbingoZTT.BanmaReader.dao.Chapter;
import com.codingbingoZTT.BanmaReader.dao.ChapterDao;
import com.codingbingoZTT.BanmaReader.model.eventbus.BookStatusChangeEvent;
import com.codingbingoZTT.BanmaReader.ui.adapter.ChapterListAdapter;
import com.codingbingoZTT.BanmaReader.ui.listener.OnChapterClickListener;
import com.codingbingoZTT.BanmaReader.utils.SimpleDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * By 2017/3/30.
 */

public class ChapterListFragment extends BaseFragment implements View.OnClickListener, OnChapterClickListener {

    private RecyclerView mChapterListView;
    private ChapterListAdapter mChapterListAdapter;
    private ImageView mBackBtn;

    private String bookPath;
    private long bookId;
    private List<Chapter> mChapterList;
    private int mCurrentChapter;

    public ChapterListFragment() {
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;

        if (mChapterList == null){
            mChapterList = new ArrayList<>();
        }
        if (mChapterList.size() == 0) {
            mChapterList.addAll(getDaoSession()
                    .getChapterDao()
                    .queryBuilder()
                    .where(ChapterDao.Properties.BookId.eq(bookId)).list());
        }
        if (getDaoSession().getBookDao().load(bookId) != null) {
            mCurrentChapter = getDaoSession().getBookDao().load(bookId).getCurrentChapter();
        } else {
            mCurrentChapter = 0;
        }

        if (mChapterListAdapter != null){
            mChapterListAdapter.setmCurrentChapter(mCurrentChapter);
            mChapterListAdapter.notifyDataSetChanged();
            mChapterListView.scrollToPosition(mCurrentChapter);
        }
    }

    public void setBookPath(String bookPath) {
        this.bookPath = bookPath;
    }

    @Override
    public String getFragmentName() {
        return getClass().getSimpleName();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chapter_list, null);

        switchFullScreen(true);

        initView(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (bookId == BaseActivity.NO_BOOK_ID){
            List<Book> bookList = getDaoSession().getBookDao().queryBuilder().where(BookDao.Properties.BookPath.eq(bookPath)).list();
            if (bookList.size() > 0){
                bookId = bookList.get(0).getId();
            }
        }

        setBookId(bookId);
    }

    private void initView(View view) {
        mChapterListView = (RecyclerView) view.findViewById(R.id.chapter_list);
        mBackBtn = (ImageView) view.findViewById(R.id.back_btn);

        mChapterList = getDaoSession().getChapterDao().queryBuilder().where(ChapterDao.Properties.BookId.eq(bookId)).list();//初始化list
        if (mChapterList == null){
            mChapterList = new ArrayList<>();
        }
        mChapterListAdapter = new ChapterListAdapter(getActivity(), mChapterList, mCurrentChapter);
        mChapterListAdapter.setOnChapterClickListener(this);
        mChapterListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mChapterListView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        mChapterListView.setAdapter(mChapterListAdapter);
        //滑动到当前章节
        mChapterListView.scrollToPosition(mCurrentChapter);

        mBackBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_btn:
                getFragmentManager().popBackStack();
                break;
        }
    }

    @Override
    public void onChapterClick(int chapter) {
        mCurrentChapter = chapter;

        Book book = getDaoSession().getBookDao().load(bookId);
        book.setCurrentChapter(chapter);
        book.setCurrentPosition(mChapterList.get(chapter).getPosition());
        getDaoSession().getBookDao().update(book);

        EventBus.getDefault().post(new BookStatusChangeEvent(Constants.BOOK_PROCESSED, 100, bookId));
        getFragmentManager().popBackStack();
    }
}
