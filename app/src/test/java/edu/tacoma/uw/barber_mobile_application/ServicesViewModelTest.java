package edu.tacoma.uw.barber_mobile_application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import edu.tacoma.uw.barber_mobile_application.ui.services.ServicesViewModel;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import androidx.lifecycle.Observer;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ServicesViewModelTest {
    private ServicesViewModel mServicesViewModel;
    @Mock
    private Observer<String> mObserver;

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Before
    public void setUp() {
        mServicesViewModel = new ServicesViewModel();
    }

    @Test
    public void testInitialValue() {
        // LiveData
        mServicesViewModel.getText().observeForever(mObserver);

        // assert whether its equal to the value
        assertEquals("This is services fragment", mServicesViewModel.getText().getValue());

        // Check to see that onChanged has been changed
        verify(mObserver).onChanged("This is services fragment");

        mServicesViewModel.getText().removeObserver(mObserver);
    }
}
