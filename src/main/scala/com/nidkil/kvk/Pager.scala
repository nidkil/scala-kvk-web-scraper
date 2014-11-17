package com.nidkil.kvk

class Pager(filter: Filter, availablePages: Int) {

  private var curPage = 0

  def getCurrentPage(): Int = curPage

  def nextPage(): Option[SearchUrl] = {
    if (hasMorePages()) {
      curPage += 1
      Some(new SearchUrl(filter, curPage - 1))
    } else None
  }

  def hasMorePages(): Boolean = {
    (curPage < filter.maxpages && curPage < availablePages)
  }

}