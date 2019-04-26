package edu.epam.izhevsk.junit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.AdditionalMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;



@RunWith(MockitoJUnitRunner.class)
public class PaymentControllerTest {


    @Mock
    private AccountService accountService;

    @Mock
    private DepositService depositService;

    @InjectMocks
    private PaymentController paymentController = new PaymentController(accountService, depositService);


    @Before
    public void setUp() throws Exception {
        when(accountService.isUserAuthenticated(100L)).thenReturn(true);
        when(depositService.deposit(AdditionalMatchers.gt(100L), anyLong())).thenThrow(new InsufficientFundsException());
    }


    @Test
    public void testUserIdSuccess() throws InsufficientFundsException {
        paymentController.deposit(50L, 100L);
        verify(accountService, times(1)).isUserAuthenticated(100L);

    }

    @Test(expected = SecurityException.class)
    public void failedDepositUnauthUser() throws InsufficientFundsException {
        paymentController.deposit(49L, 105L);
    }

    @Test(expected = InsufficientFundsException.class)
    public void testLargeAmount() throws InsufficientFundsException {
        paymentController.deposit(150L, 100L);
    }


}
