package com.nphc.hr.services;

import com.nphc.hr.dto.EmployeeDto;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service
public class FilterService {
    public void filterByOffset(int offset, List<EmployeeDto> employeeList){
        Iterator<EmployeeDto> employeeIterator = employeeList.iterator();
        if(offset != 0 && employeeList.size() > offset) {
            int empIndex = 0;
            while (employeeIterator.next() != null) {
                if (empIndex < offset) {
                    employeeIterator.remove();
                    empIndex++;
                } else {
                    break;
                }
            }
        }
    }

    public void filterByLimit(int limit, List<EmployeeDto> employeeList){
        if(limit > 0 && employeeList.size() > limit){
            int removed = 0;
            int numRemoval = employeeList.size() - limit;
            while (removed < numRemoval) {
                employeeList.remove(employeeList.size() - 1);
                removed++;
            }
        }
    }
}
