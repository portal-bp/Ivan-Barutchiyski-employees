package com.ib.ibempapp.entity;

import java.time.LocalDate;

public record Project (int projectId, LocalDate dateFrom, LocalDate dateTo) {}