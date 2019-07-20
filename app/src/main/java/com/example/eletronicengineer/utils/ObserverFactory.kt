package com.example.eletronicengineer.utils

class ObserverFactory
{
  interface RecyclerviewAdapterObserver
  {
    fun onBindComplete()
    fun onBindRunning()
  }
}