package com.example.projectmarketplace.viewModels

import androidx.lifecycle.ViewModel
import com.example.projectmarketplace.data.Order
import com.example.projectmarketplace.repositories.OrderRepository

class OrderViewModel(private val repository: OrderRepository) : ViewModel() {
    private var _orders = listOf<Order>()
    val orders: List<Order> get() = _orders


    suspend fun fetchOrders(): List<Order> {
        if (_orders.isEmpty()) {
            _orders = repository.fetchOrders()
        }
        return _orders
    }
}

