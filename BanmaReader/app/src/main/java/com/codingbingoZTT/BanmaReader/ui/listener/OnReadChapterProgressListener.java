package com.codingbingoZTT.BanmaReader.ui.listener;

import android.widget.SeekBar;

/**
 *
 *
 * By 2017/4/23.
 */

public interface OnReadChapterProgressListener {
    void onReadProgressChanged(SeekBar seekBar, int progress, boolean fromUser);
}
