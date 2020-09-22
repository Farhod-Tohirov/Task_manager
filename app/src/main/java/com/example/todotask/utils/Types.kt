package com.example.todotask.utils

import android.net.Uri

typealias SingleBlock <T> = (T) -> Unit
typealias DoubleBlock <T, E> = (T, E) -> Unit
typealias EmptyBlock = () -> Unit
